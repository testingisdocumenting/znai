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
import java.util.*;
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

    public AnchorIds generateIds(Path path, String nonUniqueId) {
        // we create multiple ids: long version and short version
        // both ids should work and be recognizable, but we visually expose the long version
        var prefixes = buildPrefixes(path, nonUniqueId);
        var main = updateWithCountSuffixIfRequired(path, prefixes.usingAllHeadings);
        var alternative = updateWithCountSuffixIfRequired(path, prefixes.usingLastHeading);

        boolean arePrefixesEqual = prefixes.usingAllHeadings.equals(prefixes.usingLastHeading);
        var alternativeList = arePrefixesEqual ?
                Collections.<String>emptyList():
                Collections.singletonList(alternative);
        return new AnchorIds(main, alternativeList);
    }

    private String updateWithCountSuffixIfRequired(Path path, String prefix) {
        var pathsCounts = usedHeadingsCountByPath.computeIfAbsent(path, k -> new HashMap<>());

        Integer count = pathsCounts.getOrDefault(prefix, 0);
        pathsCounts.put(prefix, count + 1);

        if (count == 0) {
            return prefix;
        }

        return prefix + "-" + (count + 1);
    }

    private Prefixes buildPrefixes(Path path, String nonUniqueId) {
        var headings = getOrCreateHeadings(path);

        String fullPrefix = headings.stream()
                .map(LevelId::getId)
                .filter(id -> !id.isEmpty())
                .collect(Collectors.joining("-"));

        String lastHeadingOnly = headings.isEmpty() ? "" : headings.get(headings.size() - 1).id;

        return new Prefixes(combinePrefixAndId(fullPrefix, nonUniqueId),
                combinePrefixAndId(lastHeadingOnly, nonUniqueId));
    }

    private String combinePrefixAndId(String prefix, String id) {
        if (prefix.isEmpty()) {
            return id;
        }

        if (id.isEmpty()) {
            return prefix;
        }

        return prefix + "-" + id;
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

    record Prefixes (String usingAllHeadings, String usingLastHeading) {}
}