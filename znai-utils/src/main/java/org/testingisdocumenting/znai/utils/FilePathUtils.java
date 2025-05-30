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

package org.testingisdocumenting.znai.utils;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathUtils {
    private FilePathUtils() {
    }

    public static String fileNameWithoutExtension(String path) {
        return fileNameWithoutExtension(Paths.get(path));
    }

    public static String fileNameWithoutExtension(Path path) {
        return FilenameUtils.removeExtension(path.getFileName().toString());
    }

    public static String replaceExtension(String path, String newExt) {
        return FilenameUtils.removeExtension(path) + "." + newExt;
    }

    public static String fileExtension(String path) {
        return FilenameUtils.getExtension(path);
    }
}
