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
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MonitorConfig {
    private final int intervalMillis;
    private final List<BuildRootAndWildCardPatterns> buildRootsAndPatterns;

    public MonitorConfig(Map<String, Object> config) {
        this.intervalMillis = extractInterval(config, (int) TimeUnit.MINUTES.toMillis(1));
        this.buildRootsAndPatterns = extractRootAndPatterns(config);
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    public List<BuildRootAndWildCardPatterns> getBuildRootsAndPatterns() {
        return buildRootsAndPatterns;
    }

    private static Path extractRootDir(Map<String, Object> config) {
        return Paths.get(config.getOrDefault("rootDir", "").toString());
    }

    private static int extractInterval(Map<String, Object> config, int defaultValue) {
        return (int) config.getOrDefault("intervalMillis", defaultValue);
    }

    @SuppressWarnings("unchecked")
    private static List<BuildRootAndWildCardPatterns> extractRootAndPatterns(Map<String, Object> config) {
        List<Map<String, Object>> paths =
                (List<Map<String, Object>>) config.getOrDefault("paths", Collections.emptyList());

        return paths.stream()
                .map(rootAndPatterns -> new BuildRootAndWildCardPatterns(
                        extractRootDir(rootAndPatterns),
                        extractWildCardPatterns(rootAndPatterns)))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static List<String> extractWildCardPatterns(Map<String, Object> config) {
        return (List<String>) config.getOrDefault("wildCardPatterns", Collections.emptyList());
    }
}
