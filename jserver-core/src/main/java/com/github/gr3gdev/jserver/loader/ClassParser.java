package com.github.gr3gdev.jserver.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * ClassParser.
 *
 * @author Gregory Tardivel
 */
public class ClassParser {

    public static Set<Class<?>> findAllClasses() {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final List<File> folders = new ArrayList<>();
        try {
            final Enumeration<URL> resources = classLoader.getResources("");
            while (resources.hasMoreElements()) {
                final URL url = resources.nextElement();
                folders.add(new File(url.getFile()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Set<Class<?>> classes = new HashSet<>();
        folders.forEach(f -> classes.addAll(findClasses(f, "")));
        return classes;
    }

    private static Collection<Class<?>> findClasses(File dir, String packageName) {
        final Set<Class<?>> classes = new HashSet<>();
        final String packageNameCleaned;
        if (packageName.startsWith(".")) {
            packageNameCleaned = packageName.substring(1);
        } else {
            packageNameCleaned = packageName;
        }
        final File[] files = dir.listFiles(pathname -> !pathname.getName().equals("module-info.class"));
        if (dir.exists() && files != null) {
            Arrays.stream(files).forEach(f -> {
                if (f.isDirectory()) {
                    classes.addAll(findClasses(f, packageNameCleaned + "." + f.getName()));
                } else if (f.getName().endsWith(".class")) {
                    try {
                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageNameCleaned + "." + f.getName().substring(0, f.getName().length() - 6)));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return classes;
    }
}
