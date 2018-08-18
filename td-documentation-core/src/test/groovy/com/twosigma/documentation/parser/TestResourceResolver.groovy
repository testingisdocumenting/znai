package com.twosigma.documentation.parser

import com.twosigma.documentation.core.ResourcesResolver
import com.twosigma.utils.ResourceUtils

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

/**
 * @author mykola
 */
class TestResourceResolver implements ResourcesResolver {
    private Path root

    TestResourceResolver(Path root) {
        this.root = root.toAbsolutePath()
    }

    @Override
    void initialize(Stream<String> filteredLookupPaths) {
    }

    @Override
    boolean supportsLookupPath(String lookupPath) {
        return true
    }

    @Override
    boolean canResolve(String path) {
        return true
    }

    @Override
    List<String> listOfTriedLocations(String path) {
        return []
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
    boolean isInsideDoc(Path path) {
        return path.toAbsolutePath().startsWith(root)
    }

    @Override
    boolean isLocalFile(String path) {
        return ResourceUtils.resourceStream(path) != null
    }
}
