/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.enterprise.monitoring;

import org.apache.tools.ant.DirectoryScanner;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FilesFinder {
    private final List<BuildRootAndWildCardPatterns> buildRootAndWildCardPatterns;

    public FilesFinder(List<BuildRootAndWildCardPatterns> buildRootAndWildCardPatterns) {
        this.buildRootAndWildCardPatterns = buildRootAndWildCardPatterns;
    }

    public List<Path> find() {
        return buildRootAndWildCardPatterns.stream()
                .flatMap(rootAndPatterns -> find(rootAndPatterns).stream())
                .collect(Collectors.toList());
    }

    private List<Path> find(BuildRootAndWildCardPatterns buildRootAndWildCardPatterns) {
        ConsoleOutputs.out("scanning ", Color.PURPLE, buildRootAndWildCardPatterns.getBuildRoot(),
                Color.WHITE, " patterns\n  ", String.join("\n  ", buildRootAndWildCardPatterns.getWildCardPatterns()));

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(buildRootAndWildCardPatterns.getBuildRoot().toFile());
        scanner.setIncludes(buildRootAndWildCardPatterns.getWildCardPatterns().toArray(new String[0]));

        try {
            scanner.scan();
        } catch (Exception e) {
            ConsoleOutputs.err("scan error: " + e.getMessage());
            return Collections.emptyList();
        }

        return Arrays.stream(scanner.getIncludedFiles())
                .map(filePath -> buildRootAndWildCardPatterns.getBuildRoot().resolve(filePath))
                .collect(Collectors.toList());
    }
}
