//package com.twosigma.documentation.website.preview;
//
//import java.io.IOException;
//import java.nio.file.FileSystems;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.WatchEvent;
//import java.nio.file.WatchEvent.Kind;
//import java.nio.file.WatchKey;
//import java.nio.file.WatchService;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Stream;
//
//import com.twosigma.cue.doc.gen.console.ConsoleOutputs;
//
//import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
//import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
//import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
//
///**
// * @author mykola
// */
//public class FileWatcher {
//    private FileChangeHandler fileChangeHandler;
//    private final WatchService watchService;
//    private Map<WatchKey, Path> pathByKey;
//
//    public FileWatcher(FileChangeHandler fileChangeHandler) {
//        this.fileChangeHandler = fileChangeHandler;
//        watchService = createWatchService();
//        pathByKey = new HashMap<>();
//        final Path rootPath = Paths.get("").toAbsolutePath();
//        register(rootPath);
//        registerDirs(rootPath);
//    }
//
//    public void start() {
//        try {
//            startWatchLoop();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void startWatchLoop() throws InterruptedException {
//        while (true) {
//            try {
//                watchCycle();
//            } catch (RuntimeException e) {
//                ConsoleOutputs.err(e.getMessage());
//            }
//        }
//    }
//
//    private void watchCycle() throws InterruptedException {
//        final WatchKey key = watchService.take();
//        try {
//            final Path path = pathByKey.get(key);
//            if (path == null) {
//                ConsoleOutputs.err("bad watch key: ", key);
//                return;
//            }
//
//            key.pollEvents().forEach(e -> handleEvent(path, e));
//        } finally {
//            boolean isValid = key.reset();
//            if (!isValid) {
//                pathByKey.remove(key);
//            }
//        }
//    }
//
//    private void handleEvent(Path parentPath, final WatchEvent<?> watchEvent) {
//        final Kind<?> kind = watchEvent.kind();
//        if (kind == OVERFLOW)
//            return;
//
//        final Path relativePath = ((WatchEvent<Path>) watchEvent).context();
//        final Path path = parentPath.resolve(relativePath);
//
//        ConsoleOutputs.out("watch event: ", kind, " context: ", path);
//
//        if (kind == ENTRY_CREATE) {
//            handleCreate(path);
//        } else if (kind == ENTRY_MODIFY) {
//            handleModify(path);
//        }
//    }
//
//    private void handleCreate(final Path path) {
//        if (Files.isDirectory(path)) {
//            register(path);
//        }
//    }
//
//    private void handleModify(final Path path) {
//        final String fileName = path.getFileName().toString();
//
//        if (fileName.equals("toc")) {
//            fileChangeHandler.onTocChange(path);
//        } else if (fileName.endsWith(".md")) {
//            fileChangeHandler.onMdChange(path);
//        } else {
//            fileChangeHandler.onChange(path);
//        }
//    }
//
//    private void register(Path path) {
//        try {
//            final WatchKey key = path.register(watchService, ENTRY_MODIFY, ENTRY_CREATE);
//            pathByKey.put(key, path);
//
//            ConsoleOutputs.out("watching: ", path);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void registerDirs(final Path rootPath) {
//        try {
//            final Stream<Path> pathStream = Files.list(rootPath);
//            pathStream.filter(p -> Files.isDirectory(p)).forEach(this::register);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private WatchService createWatchService() {
//        final WatchService watchService;
//        try {
//            watchService = FileSystems.getDefault().newWatchService();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return watchService;
//    }
//}
