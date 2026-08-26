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
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.AuxiliaryFileListener;
import org.testingisdocumenting.znai.core.DocMeta;
import org.testingisdocumenting.znai.website.TocChangeListener;
import org.testingisdocumenting.znai.website.WebSite;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher implements AuxiliaryFileListener, TocChangeListener {
    private final WebSite.Configuration siteCfg;
    private final FileChangeHandler fileChangeHandler;
    private final WatchService watchService;
    private final Map<WatchKey, Path> pathByKey;
    private final Map<Path, WatchKey> keyByPath;
    // directories that were watched but disappeared (e.g. deleted during git rebase);
    // checked every watch cycle to resume watching once they are re-created
    private final Set<Path> droppedPaths;
    private final AtomicBoolean isTerminated = new AtomicBoolean(false);

    private static final Path tempDirPath = detectTempFilesDir();

    public FileWatcher(WebSite.Configuration siteCfg, Stream<Path> pathsToWatch, FileChangeHandler fileChangeHandler) {
        this.siteCfg = siteCfg;
        this.fileChangeHandler = fileChangeHandler;

        watchService = createWatchService();
        pathByKey = new HashMap<>();
        keyByPath = new HashMap<>();
        droppedPaths = new LinkedHashSet<>();

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

    public void stop() {
        isTerminated.set(true);
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
                if (isTerminated.get()) {
                    ConsoleOutputs.out("previous file watcher stopped for root: ", Color.PURPLE, siteCfg.getDocRootPath());
                    break;
                }
            } catch (RuntimeException e) {
                ConsoleOutputs.err(e.getClass() + ":" + e.getMessage());
            }
        }
    }

    private void watchCycle() throws InterruptedException {
        reRegisterDroppedPaths();

        final WatchKey key = watchService.poll(1000, TimeUnit.MILLISECONDS);

        if (key == null) {
            return;
        }

        try {
            if (isTerminated.get()) {
                return;
            }

            final Path path = pathByKey.get(key);
            if (path == null) {
                if (key.isValid()) {
                    ConsoleOutputs.err("bad watch key: ", key);
                }

                return;
            }

            key.pollEvents().forEach(e -> handleEvent(path, e));
        } finally {
            boolean isValid = key.reset();
            if (!isValid) {
                markDropped(key);
            }
        }
    }

    private void markDropped(WatchKey key) {
        Path path = pathByKey.remove(key);
        if (path == null) {
            return;
        }

        keyByPath.remove(path, key);
        droppedPaths.add(path);
        ConsoleOutputs.out("stopped watching (directory is gone): ", Color.PURPLE, path);
    }

    // a git rebase (or branch switch) can delete a watched directory and re-create it a moment later;
    // a watch key of a deleted directory is invalidated forever, so watching is resumed
    // explicitly once the directory shows up again
    private void reRegisterDroppedPaths() {
        if (droppedPaths.isEmpty() || isTerminated.get()) {
            return;
        }

        List<Path> reAppeared = new ArrayList<>();
        for (Path path : droppedPaths) {
            if (Files.isDirectory(path)) {
                reAppeared.add(path);
            }
        }

        droppedPaths.removeAll(reAppeared);
        reAppeared.forEach(this::registerDirAndHandleMissedChanges);
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
            registerDirAndHandleMissedChanges(path);
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

    // registers a directory that just appeared (e.g. created by a user or re-created by a git rebase).
    // its content may have been created before the watch was established, so nested directories are
    // registered explicitly and files are treated as changed as their events may have been missed
    private void registerDirAndHandleMissedChanges(Path dir) {
        if (isWatched(dir) || shouldIgnore(dir)) {
            return;
        }

        register(dir);

        try (Stream<Path> children = Files.list(dir)) {
            children.forEach(child -> {
                if (Files.isDirectory(child)) {
                    if (!Files.isSymbolicLink(child)) {
                        registerDirAndHandleMissedChanges(child);
                    }
                } else {
                    handleModify(child);
                }
            });
        } catch (IOException e) {
            ConsoleOutputs.err("can't list directory " + dir + ": " + e.getMessage());
        }
    }

    boolean isWatched(Path dir) {
        WatchKey key = keyByPath.get(dir);
        return key != null && key.isValid();
    }

    private static boolean shouldIgnore(Path dir) {
        return dir.endsWith(".vertx") || dir.endsWith(".idea") || tempDirPath.equals(dir);
    }

    private void register(Path path) {
        try {
            if (!Files.exists(path)) {
                return;
            }

            if (!Files.isDirectory(path)) {
                path = path.getParent();
            }

            if (shouldIgnore(path)) {
                return;
            }

            if (isWatched(path)) {
                return;
            }

            WatchKey staleKey = keyByPath.remove(path);
            if (staleKey != null) {
                // directory was deleted and re-created (e.g. during git rebase), old key is unusable
                staleKey.cancel();
                pathByKey.remove(staleKey);
            }

            droppedPaths.remove(path);

            // ENTRY_CREATE is required to handle swap in atomic writes
            final WatchKey key = path.register(watchService,
                    new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY},
                    SensitivityWatchEventModifier.HIGH);
            pathByKey.put(key, path);
            keyByPath.put(path, key);

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
