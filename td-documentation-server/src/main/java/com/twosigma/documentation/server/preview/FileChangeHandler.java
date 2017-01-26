package com.twosigma.documentation.server.preview;

import java.nio.file.Path;

/**
 * @author mykola
 */
public interface FileChangeHandler {
    void onTocChange(Path path);
    void onMarkupChange(Path path);

    void onChange(Path path);
}