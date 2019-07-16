package com.twosigma.documentation.core;

import com.twosigma.documentation.structure.TocItem;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class AuxiliaryFilesRegistry {
    private final Map<Path, Set<TocItem>> tocItemsByAuxiliaryFilePath;
    private final Map<TocItem, Set<AuxiliaryFile>> auxiliaryFilesByTocItem;
    private final Map<Path, AuxiliaryFile> auxiliaryFiles;
    private final Set<AuxiliaryFileListener> auxiliaryFileListeners;

    public AuxiliaryFilesRegistry() {
        this.tocItemsByAuxiliaryFilePath = new HashMap<>();
        this.auxiliaryFilesByTocItem = new HashMap<>();
        this.auxiliaryFiles = new HashMap<>();
        this.auxiliaryFileListeners = new HashSet<>();
    }

    public Set<TocItem> tocItemsByPath(Path auxiliaryFile) {
        return tocItemsByAuxiliaryFilePath.getOrDefault(auxiliaryFile, Collections.emptySet());
    }

    public Set<AuxiliaryFile> auxiliaryFilesByTocItem(TocItem tocItem) {
        return auxiliaryFilesByTocItem.getOrDefault(tocItem, Collections.emptySet());
    }

    public void updateFileAssociations(TocItem tocItem, AuxiliaryFile auxiliaryFile) {
        Set<TocItem> tocItems = tocItemsByAuxiliaryFilePath.computeIfAbsent(auxiliaryFile.getPath(), k -> new HashSet<>());
        tocItems.add(tocItem);

        Set<AuxiliaryFile> filesForTocItem = auxiliaryFilesByTocItem.computeIfAbsent(tocItem, k -> new HashSet<>());
        filesForTocItem.add(auxiliaryFile);

        if (!this.auxiliaryFiles.containsKey(auxiliaryFile.getPath()) || !this.auxiliaryFiles.get(auxiliaryFile.getPath()).isDeploymentRequired()) {
            this.auxiliaryFiles.put(auxiliaryFile.getPath(), auxiliaryFile);
        }

        auxiliaryFileListeners.forEach(listener -> listener.onAuxiliaryFile(auxiliaryFile));
    }

    public AuxiliaryFile auxiliaryFileByPath(Path path) {
        return auxiliaryFiles.get(path);
    }

    public Stream<AuxiliaryFile> getAuxiliaryFilesForDeployment() {
        return auxiliaryFiles.values().stream().filter(AuxiliaryFile::isDeploymentRequired);
    }

    public boolean requiresDeployment(Path path) {
        AuxiliaryFile auxiliaryFile = auxiliaryFiles.get(path);
        return auxiliaryFile != null && auxiliaryFile.isDeploymentRequired();
    }

    public Stream<Path> getAllPaths() {
        return auxiliaryFiles.keySet().stream();
    }

    public void registerListener(AuxiliaryFileListener listener) {
        auxiliaryFileListeners.add(listener);
    }

    public void unregisterListener(AuxiliaryFileListener listener) {
        auxiliaryFileListeners.remove(listener);
    }
}
