package com.twosigma.documentation.parser

import com.twosigma.documentation.core.ResourcesResolver
import com.twosigma.utils.ResourceUtils

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class TestResourceResolver implements ResourcesResolver {
    private Path root

    TestResourceResolver(Path root) {
        this.root = root
    }

    TestResourceResolver() {
    }

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
        return ImageIO.read(ResourceUtils.requiredResourceStream(path))
    }

    @Override
    Path fullPath(String path) {
        return root ? root.resolve(path) : Paths.get(path).toAbsolutePath()
    }

    @Override
    Path docRootRelativePath(Path path) {
        return path.fileName
    }

    @Override
    boolean exists(String path) {
        return ResourceUtils.resourceStream(path) != null
    }
}
