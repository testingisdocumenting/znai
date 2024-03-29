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

package org.testingisdocumenting.znai.structure;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UniqueAnchorIdGenerator {
    private final Map<Path, List<LevelId>> headingsByPath;
    private final Map<Path, Map<String, Integer>> usedHeadingsCountByPath;

    public UniqueAnchorIdGenerator() {
        headingsByPath = new ConcurrentHashMap<>();
        usedHeadingsCountByPath = new ConcurrentHashMap<>();
    }

    public void registerSectionOrSubHeading(Path path, Integer level, String id) {
        var headings = getOrCreateHeadings(path);
        headings.removeIf(levelId -> levelId.level >= level);
        headings.add(new LevelId(level, id));
    }

    public String generateId(Path path, String nonUniqueId) {
        var headings = getOrCreateHeadings(path);
        String prefix = headings.stream()
                .map(LevelId::getId)
                .filter(id -> !id.isEmpty())
                .collect(Collectors.joining("-"));

        if (!nonUniqueId.isEmpty()) {
            prefix = prefix + (prefix.isEmpty() ? nonUniqueId : "-" + nonUniqueId);
        }

        var usedHeadingsCount = usedHeadingsCountByPath.computeIfAbsent(path, k -> new HashMap<>());
        Integer count = usedHeadingsCount.get(prefix);
        if (count == null) {
            usedHeadingsCount.put(prefix, 1);
            return prefix;
        }

        usedHeadingsCount.put(prefix, count + 1);
        return prefix + "-" + (count + 1);
    }

    private List<LevelId> getOrCreateHeadings(Path path) {
        return headingsByPath.computeIfAbsent(path, k -> new ArrayList<>());
    }

    public void resetCountersIfPresent(Path path) {
        headingsByPath.remove(path);
        usedHeadingsCountByPath.remove(path);
    }

    static class LevelId {
        private final Integer level;
        private final String id;

        public LevelId(Integer level, String id) {
            this.level = level;
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}