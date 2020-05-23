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

import java.nio.file.Path;
import java.util.List;

public class BuildRootAndWildCardPatterns {
    private final Path buildRoot;
    private final List<String> wildCardPatterns;

    public BuildRootAndWildCardPatterns(Path buildRoot, List<String> wildCardPatterns) {
        this.buildRoot = buildRoot.toAbsolutePath();
        this.wildCardPatterns = wildCardPatterns;
    }

    public Path getBuildRoot() {
        return buildRoot;
    }

    public List<String> getWildCardPatterns() {
        return wildCardPatterns;
    }
}
