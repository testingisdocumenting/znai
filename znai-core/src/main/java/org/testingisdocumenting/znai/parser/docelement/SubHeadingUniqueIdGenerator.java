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

package org.testingisdocumenting.znai.parser.docelement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class SubHeadingUniqueIdGenerator {
    private final List<LevelId> subheadings;
    private final Map<String, Integer> usedSubheadingsCount;

    SubHeadingUniqueIdGenerator(String topPrefix) {
        subheadings = new ArrayList<>();
        subheadings.add(new LevelId(1, topPrefix));

        usedSubheadingsCount = new HashMap<>();
    }

    void registerSubHeading(Integer level, String id) {
        subheadings.removeIf(levelId -> levelId.level >= level);
        subheadings.add(new LevelId(level, id));
    }

    String generateId() {
        String prefix = subheadings.stream()
                .map(LevelId::getId)
                .filter(id -> !id.isEmpty())
                .collect(Collectors.joining("-"));

        Integer count = usedSubheadingsCount.get(prefix);
        if (count == null) {
            usedSubheadingsCount.put(prefix, 1);
            return prefix;
        }

        usedSubheadingsCount.put(prefix, count + 1);
        return prefix + "-" + (count + 1);
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