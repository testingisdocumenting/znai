package com.twosigma.documentation.parser

import com.twosigma.documentation.extensions.PluginResourcesResolver
import com.twosigma.utils.ResourceUtils

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class TestResourceResolver implements PluginResourcesResolver {
    @Override
    String textContent(String path) {
        return ResourceUtils.textContent(path)
    }

    @Override
    String textContent(Path path) {
        return ResourceUtils.textContent(path.fileName.toString())
    }

    @Override
    BufferedImage imageContent(String path) {
        return ImageIO.read(ResourceUtils.resourceStream(path))
    }

    @Override
    Path fullPath(String path) {
        return Paths.get(path).toAbsolutePath()
    }

    @Override
    Path docRootRelativePath(Path path) {
        return path.fileName
    }
}
