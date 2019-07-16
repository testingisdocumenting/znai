package com.twosigma.documentation.client;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeployTempDir {
    public static Path prepare(String mode) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        Path path = Paths.get(tmpDir).toAbsolutePath().resolve("mdoc" + (mode.isEmpty() ? "" : "-" + mode));
        try {
            FileUtils.deleteQuietly(path.toFile());
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return path;
    }
}
