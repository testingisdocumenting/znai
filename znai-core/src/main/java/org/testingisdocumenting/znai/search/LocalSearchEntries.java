/*
 * Copyright 2024 znai maintainers
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

package org.testingisdocumenting.znai.search;

import org.testingisdocumenting.znai.structure.TocItem;
import org.testingisdocumenting.znai.utils.JsonUtils;
import org.testingisdocumenting.znai.utils.ResourceUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LocalSearchEntries {
    private final Map<TocItem, PageSearchEntries> entries;

    public LocalSearchEntries() {
        this.entries = new LinkedHashMap<>();
    }

    public void add(PageSearchEntries entries) {
        this.entries.put(entries.getTocItem(), entries);
    }

    public PageSearchEntries searchEntriesByTocItem(TocItem tocItem) {
        return entries.get(tocItem);
    }

    public List<List<String>> toListOfLists() {
        return entries.values().stream()
                .flatMap(e -> e.toListOfLists().stream())
                .collect(Collectors.toList());
    }

    public String buildIndexScript() {
        return "znaiSearchData = " + JsonUtils.serialize(toListOfLists()) + "\n" +
                ResourceUtils.textContent("lunrjs/indexCreation.js");
    }
}
