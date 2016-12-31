package com.twosigma.documentation.website.preview;

import java.nio.file.Path;

/**
 * @author mykola
 */
public interface FileChangeHandler {
    void onTocChange(final Path path);
    void onMdChange(final Path path);

    void onChange(Path path);
}
