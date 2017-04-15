package com.twosigma.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class FileUtils {
    private FileUtils() {
    }

    public static void writeTextContent(Path path, String text) {
        try {
            Files.write(path, text.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fileTextContent(Path path) {
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
