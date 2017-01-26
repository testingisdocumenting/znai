package com.twosigma.documentation.server.preview;

import com.sun.nio.file.SensitivityWatchEventModifier;
import com.twosigma.console.ConsoleOutputs;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * @author mykola
 */
public class FileWatcher {
    private FileChangeHandler fileChangeHandler;
    private final WatchService watchService;
    private Map<WatchKey, Path> pathByKey;

    public FileWatcher(Path root, FileChangeHandler fileChangeHandler) {
        this.fileChangeHandler = fileChangeHandler;
        watchService = createWatchService();
        pathByKey = new HashMap<>();
        final Path absoluteRoot = root.toAbsolutePath();
        register(absoluteRoot);
        registerDirs(absoluteRoot);
    }

    public void start() {
        try {
            startWatchLoop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void startWatchLoop() throws InterruptedException {
        while (true) {
            try {
                watchCycle();
            } catch (RuntimeException e) {
                ConsoleOutputs.err(e.getClass() + ":" + e.getMessage());
            }
        }
    }

    private void watchCycle() throws InterruptedException {
        final WatchKey key = watchService.take();
        try {
            final Path path = pathByKey.get(key);
            if (path == null) {
                ConsoleOutputs.err("bad watch key: ", key);
                return;
            }

            key.pollEvents().forEach(e -> handleEvent(path, e));
        } finally {
            boolean isValid = key.reset();
            if (!isValid) {
                pathByKey.remove(key);
            }
        }
    }

    private void handleEvent(Path parentPath, final WatchEvent<?> watchEvent) {
        final WatchEvent.Kind<?> kind = watchEvent.kind();
        if (kind == OVERFLOW)
            return;

        @SuppressWarnings("unchecked")
        final Path relativePath = ((WatchEvent<Path>) watchEvent).context();
        final Path path = parentPath.resolve(relativePath);

        ConsoleOutputs.out("watch event: ", kind, " context: ", path);

        if (kind == ENTRY_CREATE) {
            handleCreate(path);
        } else if (kind == ENTRY_MODIFY) {
            handleModify(path);
        }
    }

    private void handleCreate(final Path path) {
        if (Files.isDirectory(path)) {
            register(path);
        }
    }

    private void handleModify(final Path path) {
        final String fileName = path.getFileName().toString();

        if (fileName.equals("toc")) {
            fileChangeHandler.onTocChange(path);
        } else if (isMarkup(fileName)) {
            fileChangeHandler.onMarkupChange(path);
        } else {
            fileChangeHandler.onChange(path);
        }
    }

    private boolean isMarkup(String fileName) {
        return fileName.endsWith(".md");
    }

    private void register(Path path) {
        try {
            final WatchKey key = path.register(watchService, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_MODIFY},
                    SensitivityWatchEventModifier.HIGH);
            pathByKey.put(key, path);

            ConsoleOutputs.out("watching: ", path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerDirs(final Path rootPath) {
        try {
            final Stream<Path> pathStream = Files.list(rootPath);
            pathStream.filter(p -> Files.isDirectory(p)).forEach(this::register);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private WatchService createWatchService() {
        final WatchService watchService;
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return watchService;
    }
}
