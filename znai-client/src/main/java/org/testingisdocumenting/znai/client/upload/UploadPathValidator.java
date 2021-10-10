/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.client.upload;

import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UploadPathValidator {
    private static final long MAX_MB_COUNT = 40;
    private static final long MB = 1000 * 1000;
    private static final long MAX_SIZE = MAX_MB_COUNT * MB;

    public static void validate(Path path, String file) {
        if (!path.endsWith(".zip") && !Files.exists(path.resolve(file))) {
            throw new RuntimeException(String.format("no %s found in %s", file, path));
        }

        ConsoleOutputs.out(Color.BLUE, "validating size of ", Color.PURPLE, path);

        long size = getSize(path);
        if (size > MAX_SIZE) {
            throw new RuntimeException(String.format("upload documentation size should be less than %dMb, received: %d(%s)", MAX_MB_COUNT, size, humanReadable(size)));
        }
    }

    private static String humanReadable(final long size) {
        return String.format("%.2fMb", (size / (double) MB));
    }

    private static long getSize(final Path path) {
        try {
            return Files.walk(path)
                    .filter(p -> p.toFile().isFile())
                    .mapToLong(p -> p.toFile().length())
                    .sum();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
