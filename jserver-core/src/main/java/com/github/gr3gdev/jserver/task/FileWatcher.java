package com.github.gr3gdev.jserver.task;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public abstract class FileWatcher extends TimerTask {

    private final URL[] urls;
    private final Map<String, Long> changes = new HashMap<>();

    public FileWatcher(URL[] urls) {
        this.urls = urls;
        readLastModifiedTime();
    }

    private void readLastModifiedTime() {
        Arrays.stream(this.urls).forEach(url -> {
            try {
                final Path path = Paths.get(url.toURI());
                final Set<Path> files = Files.list(path).collect(Collectors.toSet());
                for (final Path file : files) {
                    final Long lastModified = changes.get(file.toString());
                    final long lastModifiedTime = Files.getLastModifiedTime(file).toMillis();
                    if (!Objects.equals(lastModified, lastModifiedTime)) {
                        onChange();
                    }
                    changes.put(file.toString(), lastModifiedTime);
                }
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        readLastModifiedTime();
    }

    public abstract void onChange();

}
