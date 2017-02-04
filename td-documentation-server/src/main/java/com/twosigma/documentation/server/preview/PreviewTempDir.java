package com.twosigma.documentation.server.preview;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class PreviewTempDir {
    public static Path get() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        Path path = Paths.get(tmpDir).toAbsolutePath().resolve("mdoc-preview");
        try {
            FileUtils.deleteQuietly(path.toFile());
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return path;
    }
}
