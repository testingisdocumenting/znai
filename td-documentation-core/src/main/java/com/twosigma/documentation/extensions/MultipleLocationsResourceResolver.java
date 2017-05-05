package com.twosigma.documentation.extensions;

import com.twosigma.documentation.extensions.PluginResourcesResolver;
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
public class MultipleLocationsResourceResolver implements PluginResourcesResolver {
    private final List<Path> lookupPaths;
    private Path currentFilePath;

    public MultipleLocationsResourceResolver(Stream<Path> paths) {
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
        Path original = Paths.get(path);

        Supplier<Stream<Path>> createAllLocationsStream = () -> {
            Stream<Path> relativeToCurrent = currentFilePath == null ? Stream.empty() : Stream.of(currentFilePath.getParent().resolve(path));
            Stream<Path> absoluteLocation = original.isAbsolute() ? Stream.of(original) : Stream.empty();
            Stream<Path> lookedUpInLocations = lookupPaths.stream().map(p -> p.resolve(path).normalize());

            return Stream.concat(relativeToCurrent, Stream.concat(absoluteLocation, lookedUpInLocations));
        };

        return createAllLocationsStream.get().filter(Files::exists).findFirst()
                .orElseThrow(() -> new RuntimeException("can't find any of the following files:\n" +
                        createAllLocationsStream.get().map(Path::toString).collect(Collectors.joining("\n"))));
    }

    public void setCurrentFilePath(Path currentFilePath) {
        this.currentFilePath = currentFilePath;
    }
}
