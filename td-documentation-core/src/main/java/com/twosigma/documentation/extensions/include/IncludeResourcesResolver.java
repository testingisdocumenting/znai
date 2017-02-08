package com.twosigma.documentation.extensions.include;

import java.nio.file.Path;

/**
 * @author mykola
 */
public interface IncludeResourcesResolver {
    String textContent(String path);
    default String textContent(Path path) {
        return textContent(path.toString());
    }

    Path fullPath(String path);
}
