/*
 * Copyright 2022 znai maintainers
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PageSearchEntries {
    private final TocItem tocItem;
    private final List<PageSearchEntry> entries;

    public PageSearchEntries(TocItem tocItem, List<PageSearchEntry> entries) {
        this.tocItem = tocItem;
        this.entries = entries;
    }

    public TocItem getTocItem() {
        return tocItem;
    }

    public List<PageSearchEntry> getEntries() {
        return entries;
    }

    public List<List<String>> toListOfLists() {
        return entries.stream()
                .map(this::createList)
                .collect(Collectors.toList());
    }

    private List<String> createList(PageSearchEntry pageSearchEntry) {
        return Arrays.asList(
                genId(pageSearchEntry),
                tocItem.getChapterTitle(),
                tocItem.getPageTitle(),
                pageSearchEntry.getPageSectionTitle(),
                pageSearchEntry.getSearchText().getText());
    }

    private String genId(PageSearchEntry pageSearchEntry) {
        return tocItem.getDirName() + "@@" + tocItem.getFileNameWithoutExtension() + "@@" + pageSearchEntry.getPageSectionId();
    }
}
