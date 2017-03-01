package com.twosigma.documentation.extensions.include;

import com.twosigma.utils.FileUtils;

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
public class MultipleLocationsResourceResolver implements IncludeResourcesResolver {
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
