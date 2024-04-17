/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.server.preview;

import com.sun.nio.file.SensitivityWatchEventModifier;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.AuxiliaryFileListener;
import org.testingisdocumenting.znai.core.DocMeta;
import org.testingisdocumenting.znai.website.TocChangeListener;
import org.testingisdocumenting.znai.website.WebSite;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher implements AuxiliaryFileListener, TocChangeListener {
    private final WebSite.Configuration siteCfg;
    private final FileChangeHandler fileChangeHandler;
    private final WatchService watchService;
    private final Map<WatchKey, Path> pathByKey;

    private static final Path tempDirPath = detectTempFilesDir();

    public FileWatcher(WebSite.Configuration siteCfg, Stream<Path> pathsToWatch, FileChangeHandler fileChangeHandler) {
        this.siteCfg = siteCfg;
        this.fileChangeHandler = fileChangeHandler;

        watchService = createWatchService();
        pathByKey = new HashMap<>();

        Path absoluteRoot = siteCfg.getDocRootPath().toAbsolutePath();
        register(absoluteRoot);
        pathsToWatch.forEach(this::register);
    }

    public void start() {
        try {
            startWatchLoop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAuxiliaryFile(AuxiliaryFile auxiliaryFile) {
        register(auxiliaryFile.getPath());
    }

    @Override
    public void onTocResolvedFiles(Collection<Path> files) {
        files.forEach(this::register);
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

    private void handleModify(Path path) {
        path = path.normalize();
        final String fileName = path.getFileName().toString();

        if (Files.isDirectory(path)) {
            register(path);
        } else if (fileName.equals("toc")) {
            fileChangeHandler.onTocChange(path);
        } else if (path.equals(siteCfg.getFooterPath())) {
            fileChangeHandler.onFooterChange(path);
        } else if (fileName.equals(DocMeta.META_FILE_NAME)) {
            fileChangeHandler.onDocMetaChange(path);
        } else if (path.equals(siteCfg.getGlobalReferencesPathNoExt())) {
            fileChangeHandler.onGlobalDocReferencesChange(path);
        } else {
            fileChangeHandler.onChange(path);
        }
    }

    private void register(Path path) {
        try {
            if (!Files.exists(path)) {
                return;
            }

            if (!Files.isDirectory(path)) {
                path = path.getParent();
            }

            if (path.endsWith(".vertx") || path.endsWith(".idea")) {
                return;
            }

            if (tempDirPath.equals(path)) {
                return;
            }

            if (pathByKey.containsValue(path)) {
                return;
            }

            final WatchKey key = path.register(watchService, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_MODIFY},
                    SensitivityWatchEventModifier.HIGH);
            pathByKey.put(key, path);

            ConsoleOutputs.out("watching: ", path);
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

    private static Path detectTempFilesDir() {
        try {
            Path tempFile = Files.createTempFile("detectTempDir", "");
            Files.delete(tempFile);

            return tempFile.getParent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
