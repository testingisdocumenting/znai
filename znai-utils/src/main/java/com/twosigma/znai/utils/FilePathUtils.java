package com.twosigma.znai.utils;

import java.nio.file.Path;

public class FilePathUtils {
    private FilePathUtils() {
    }

    public static String fileNameWithoutExtension(Path path) {
        String fileName = path.getFileName().toString();
        int lastDotIdx = fileName.lastIndexOf('.');
        if (lastDotIdx == -1) {
            return fileName;
        }

        return fileName.substring(0, lastDotIdx);
    }
}
