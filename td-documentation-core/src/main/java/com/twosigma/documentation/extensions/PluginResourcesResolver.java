package com.twosigma.documentation.extensions;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

/**
 * @author mykola
 */
public interface PluginResourcesResolver {
    String textContent(String path);
    BufferedImage imageContent(String path);
    default String textContent(Path path) {
        return textContent(path.toString());
    }

    Path fullPath(String path);
}
