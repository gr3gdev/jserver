package com.github.gr3gdev.jserver;

import com.github.gr3gdev.jserver.loader.ClassParser;
import com.github.gr3gdev.jserver.loader.JServerClassLoader;
import com.github.gr3gdev.jserver.loader.JServerParser;
import com.github.gr3gdev.jserver.task.FileWatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * JServerApp.
 *
 * @author Gregory Tardivel
 */
public class JServerApp implements Runnable {

    private final String jarApp;
    private final ClassLoader runtimeClassLoader;

    private final boolean devMode;

    public JServerApp(ClassLoader runtimeClassLoader) {
        this.jarApp = System.getProperty("appsDir", "/apps");
        this.runtimeClassLoader = runtimeClassLoader;
        final String variableDevMode = System.getenv("DEV_MODE");
        if (variableDevMode != null) {
            devMode = System.getenv("DEV_MODE").equals("1");
        } else {
            devMode = false;
        }
    }

    @Override
    public void run() {
        final ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            if (jarApp != null) {
                final URL[] urls = loadUrls(jarApp);
                // Create server
                final Server server = new Server();
                if (devMode) {
                    // Reload classpath in devMode
                    watchChanges(urls, server);
                } else {
                    Thread.currentThread().setContextClassLoader(new JServerClassLoader(urls, runtimeClassLoader));
                    // Configure server
                    configureServer(server);
                }
                server.start();
            } else {
                throw new RuntimeException("JAR_APPLICATION environment not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().setContextClassLoader(ctxClassLoader);
        }
    }

    private void configureServer(Server server) {
        final Set<Class<?>> classes = ClassParser.findAllClasses();
        JServerParser.parse(classes, server);
    }

    private void watchChanges(URL[] urls, Server server) {
        final TimerTask task = new FileWatcher(urls) {
            @Override
            public void onChange() {
                Thread.currentThread().setContextClassLoader(new JServerClassLoader(urls, runtimeClassLoader));
                System.out.println("Updating server...");
                server.clear();
                configureServer(server);
            }
        };
        // Check every second
        final Timer timer = new Timer();
        timer.schedule(task, new Date(), 5000);
    }

    private URL[] loadUrls(String jarApp) throws IOException {
        final File apps = new File(jarApp);
        final Set<URL> urls = new HashSet<>();
        final Set<File> files = new HashSet<>();
        if (devMode) {
            final File javaClasses = new File(apps, "classes/java/main");
            if (javaClasses.exists()) {
                // gradle build file
                files.add(javaClasses);
                final File resources = new File(apps, "resources/main");
                if (resources.exists()) {
                    files.add(resources);
                }
            } else {
                // If not gradle
                final File javaClassesAndResources = new File(apps, "classes");
                if (javaClassesAndResources.exists()) {
                    files.add(javaClassesAndResources);
                }
            }
        } else {
            files.addAll(Arrays.stream(Objects.requireNonNull(apps.listFiles())).collect(Collectors.toSet()));
        }
        for (final File file : files) {
            final File classPathFile;
            if (file.getName().endsWith(".jar")) {
                classPathFile = extractJar(file);
            } else {
                classPathFile = file;
            }
            System.out.println("Add to ClassLoader : " + classPathFile.getAbsolutePath());
            urls.add(classPathFile.toURI().toURL());
        }
        return urls.toArray(new URL[0]);
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        final File destFile = new File(destinationDir, zipEntry.getName());

        final String destDirPath = destinationDir.getCanonicalPath();
        final String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private File extractJar(File source) throws IOException {
        final File dest = new File("/tmp/" + source.getName());
        final byte[] buffer = new byte[1024];
        try (final ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(dest, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    final File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }
                    // write file content
                    try (final FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        return dest;
    }

    public static void main(String[] args) {
        final JServerApp worker = new JServerApp(ClassLoader.getPlatformClassLoader());
        new Thread(worker).start();
    }

}
