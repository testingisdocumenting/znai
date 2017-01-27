package com.twosigma.documentation.extensions.include;

import com.twosigma.utils.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class RelativeToFileAndRootResourceResolver implements IncludeResourcesResolver {
    private final Path rootPath;
    private Path currentFilePath;

    public RelativeToFileAndRootResourceResolver(Path root) {
        this.rootPath = root;
    }

    @Override
    public String textContent(String path) {
        Path file = fullPath(path);
        return FileUtils.fileTextContent(file);
    }

    @Override
    public Path fullPath(String path) {
        Path original = Paths.get(path);
        if (original.isAbsolute() && Files.exists(original)) {
            return original;
        }

        Path relativeToFile = currentFilePath.getParent().resolve(path);
        if (Files.exists(relativeToFile)) {
            return relativeToFile;
        }

        Path relativeToRoot = rootPath.resolve(path);
        if (Files.exists(relativeToRoot)) {
            return relativeToRoot;
        }

        throw new RuntimeException("can't find none of the following files:\n" +
                Stream.of(relativeToFile, relativeToRoot, original).map(Path::toString).
                        collect(Collectors.joining("\n")));
    }

    public void setCurrentFilePath(Path currentFilePath) {
        this.currentFilePath = currentFilePath;
    }
}
