/*
 * Copyright 2021 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    private FileUtils() {
    }

    /**
     * delete dir with sub dirs and files, ignoring errors.
     * Not using apache as we delete dirs on exit,
     * and by the time maven exits (in case of maven plugin), apache io is unloaded already
     * @param path dir to delete
     */
    public static void deleteDirQuietly(Path path) {
        // read java doc, Files.walk need close the resources.
        // try-with-resources to ensure that the stream's open directories are closed
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(FileUtils::deleteQuietly);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    public static void writeTextContent(Path path, String text) {
        try {
            Path parent = path.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

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
            try (Stream<String> linesStream = Files.lines(path)) {
                return linesStream.collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] fileBinaryContent(Path path) {
        if (!Files.exists(path)) {
            throw new RuntimeException(path.toAbsolutePath() + " doesn't exist");
        }

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileTime getLastModifiedTime(Path path) {
        try {
            return Files.getLastModifiedTime(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Path existingPathOrThrow(Path... paths) {
        List<Path> nonNull = Arrays.stream(paths).filter(Objects::nonNull).collect(Collectors.toList());

        return nonNull.stream().filter(Files::exists).findFirst().orElseThrow(() ->
                new RuntimeException("can't find any of the following files:\n" +
                        nonNull.stream().map(Path::toString).collect(Collectors.joining("\n"))));
    }

    public static void symlinkAwareCreateDirs(Path path) {
        try {
            Path dir = Files.isSymbolicLink(path)
                    ? Files.readSymbolicLink(path)
                    : path;
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyFile(Path source, Path target) {
        try {
            Files.createDirectories(target.getParent());
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteQuietly(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            // ignored
        }
    }
}
