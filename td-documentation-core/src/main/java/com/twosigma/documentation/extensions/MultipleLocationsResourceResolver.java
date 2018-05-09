package com.twosigma.documentation.extensions;

import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class MultipleLocationsResourceResolver implements ResourcesResolver {
    private final Path docRootPath;
    private final List<Path> lookupPaths;
    private ThreadLocal<Path> currentFilePath = new ThreadLocal<>();

    public MultipleLocationsResourceResolver(Path docRootPath, Stream<Path> paths) {
        this.docRootPath = docRootPath;
        this.lookupPaths = paths.collect(Collectors.toList());
    }

    @Override
    public String textContent(String path) {
        Path file = fullPath(path);
        return FileUtils.fileTextContent(file);
    }

    @Override
    public BufferedImage imageContent(String path) {
        Path fullPath = fullPath(path);
        try {
            return ImageIO.read(fullPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Can't load image " + fullPath, e);
        }
    }

    @Override
    public Path fullPath(String path) {
        return allLocationsStream(path).filter(Files::exists).findFirst()
                .orElseThrow(() -> new RuntimeException("can't find any of the following files:\n" +
                        allLocationsStream(path).map(Path::toString).collect(Collectors.joining("\n"))));
    }

    @Override
    public Path docRootRelativePath(Path path) {
        return docRootPath.relativize(path);
    }

    @Override
    public boolean isInsideDoc(Path path) {
        return path.toAbsolutePath().startsWith(docRootPath);
    }

    @Override
    public boolean exists(String path) {
        return allLocationsStream(path).anyMatch(Files::exists);
    }

    private Stream<Path> allLocationsStream(String path) {
        Path original = Paths.get(path);

        Stream<Path> relativeToCurrent = currentFilePath.get() == null ? Stream.empty() :
                Stream.of(currentFilePath.get().getParent().resolve(path));

        Stream<Path> absoluteLocation = original.isAbsolute() ? Stream.of(original) : Stream.empty();
        Stream<Path> lookedUpInLocations = lookupPaths.stream().map(p -> p.resolve(path).normalize());

        return Stream.concat(relativeToCurrent, Stream.concat(absoluteLocation, lookedUpInLocations));
    }

    public void setCurrentFilePath(Path currentFilePath) {
        this.currentFilePath.set(currentFilePath);
    }
}
