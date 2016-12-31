package com.twosigma.documentation.extensions;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class IncludeContext {
    private Path currentFilePath;

    public IncludeContext(final Path currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    public Path getCurrentFilePath() {
        return currentFilePath;
    }
}
