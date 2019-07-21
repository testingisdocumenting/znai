/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.znai.server.preview;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.znai.core.AuxiliaryFileListener;
import com.twosigma.znai.core.AuxiliaryFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FileWatcher implements AuxiliaryFileListener {
    private FileChangeHandler fileChangeHandler;
    private final WatchService watchService;
    private Map<WatchKey, Path> pathByKey;

    public FileWatcher(Path root, Stream<Path> auxiliaryFiles, FileChangeHandler fileChangeHandler) {
        this.fileChangeHandler = fileChangeHandler;
        watchService = createWatchService();
        pathByKey = new HashMap<>();
        final Path absoluteRoot = root.toAbsolutePath();
        register(absoluteRoot);
        registerDirs(absoluteRoot);
        auxiliaryFiles.forEach(this::register);
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
        if (kind == OVERFLOW) {
            return;
        }

        @SuppressWarnings("unchecked")
        final Path relativePath = ((WatchEvent<Path>) watchEvent).context();
        final Path path = parentPath.resolve(relativePath);

        ConsoleOutputs.out("watch event: ", kind, " context: ", path);

        if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
            handleModify(path);
        }
    }

    private void handleModify(final Path path) {
        final String fileName = path.getFileName().toString();

        if (Files.isDirectory(path)) {
            register(path);
        } else if (fileName.equals("toc")) {
            fileChangeHandler.onTocChange(path);
        } else if (fileName.equals("meta.json")) {
            fileChangeHandler.onDocMetaChange(path);
        } else {
            fileChangeHandler.onChange(path);
        }
    }

    private void register(Path path) {
        try {
            if (! Files.isDirectory(path)) {
                path = path.getParent();
            }

            if (pathByKey.values().contains(path)) {
                return;
            }

            // TODO
            final WatchKey key = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
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

    @Override
    public void onAuxiliaryFile(AuxiliaryFile auxiliaryFile) {
        register(auxiliaryFile.getPath());
    }
}
