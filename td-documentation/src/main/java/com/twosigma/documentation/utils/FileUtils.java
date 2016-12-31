package com.twosigma.documentation.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class FileUtils {
    public static String fileContent(final Path path) {
        if (!Files.exists(path)) {
            throw new RuntimeException(path.toAbsolutePath() + " doesn't exist");
        }

        try {
            return Files.lines(path).collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
