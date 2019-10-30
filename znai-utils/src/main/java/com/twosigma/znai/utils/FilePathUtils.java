package com.twosigma.znai.utils;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;

public class FilePathUtils {
    private FilePathUtils() {
    }

    public static String fileNameWithoutExtension(Path path) {
        return FilenameUtils.removeExtension(path.getFileName().toString());
    }
}
