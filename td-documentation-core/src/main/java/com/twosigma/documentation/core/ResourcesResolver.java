package com.twosigma.documentation.core;

import com.twosigma.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public interface ResourcesResolver {
    void initialize(Stream<String> filteredLookupPaths);
    boolean supportsLookupPath(String lookupPath);
    boolean canResolve(String path);
    List<String> listOfTriedLocations(String path);

    default String textContent(String path) {
        Path file = fullPath(path);
        return FileUtils.fileTextContent(file);
    }


    default BufferedImage imageContent(String path) {
        Path fullPath = fullPath(path);
        try {
            return ImageIO.read(fullPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Can't load image " + fullPath, e);
        }
    }

    default String textContent(Path path) {
        return textContent(path.toString());
    }

    Path fullPath(String path);
    Path docRootRelativePath(Path path);

    boolean isInsideDoc(Path path);

    boolean isLocalFile(String path);

    default AuxiliaryFile runtimeAuxiliaryFile(String origin) {
        Path fullPath = fullPath(origin);
        Path docRelativePath = docRootRelativePath(fullPath);

        Path deployRelativePath = isInsideDoc(fullPath) ?
                docRelativePath:
                Paths.get(origin);

        return AuxiliaryFile.runTime(fullPath, deployRelativePath);
    }
}
