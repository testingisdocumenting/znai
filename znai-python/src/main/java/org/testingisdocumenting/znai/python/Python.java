/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.python;

import org.testingisdocumenting.znai.utils.FileUtils;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.Map;

public class Python {
    public static final Python INSTANCE = new Python();

    private final ThreadLocal<PythonBasedPythonParser> pythonParser;

    private final Map<Path, FileTime> lastModifiedTimeByPath;
    private final Map<Path, PythonParsedFile> parsedByPath;

    public Python() {
        pythonParser = ThreadLocal.withInitial(PythonBasedPythonParser::new);

        lastModifiedTimeByPath = new HashMap<>();
        parsedByPath = new HashMap<>();
    }

    public PythonParsedFile parseOrGetCached(Path sourcePath, PythonContext context) {
        invalidateCache(sourcePath);
        PythonParsedFile cachedParsedFile = parsedByPath.get(sourcePath);
        if (cachedParsedFile != null) {
            return cachedParsedFile;
        }

        PythonParsedFile parsedFile = pythonParser.get().parse(sourcePath, context);
        cache(sourcePath, parsedFile);

        return parsedFile;
    }

    private void cache(Path sourcePath, PythonParsedFile parsedFile) {
        FileTime lastModifiedTime = FileUtils.getLastModifiedTime(sourcePath);
        lastModifiedTimeByPath.put(sourcePath, lastModifiedTime);
        parsedByPath.put(sourcePath, parsedFile);
    }

    private void invalidateCache(Path sourcePath) {
        FileTime lastModifiedTime = FileUtils.getLastModifiedTime(sourcePath);
        FileTime previousModifiedTime = lastModifiedTimeByPath.get(sourcePath);
        if (previousModifiedTime == null) {
            return;
        }

        if (!lastModifiedTime.equals(previousModifiedTime)) {
            lastModifiedTimeByPath.remove(sourcePath);
            parsedByPath.remove(sourcePath);
        }
    }
}
