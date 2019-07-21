/*
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

package com.twosigma.znai.structure;

import java.nio.file.Path;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class TableOfContents {
    private String defaultFileExtension;
    private List<TocItem> tocItems;

    public TableOfContents(String defaultFileExtension) {
        this.defaultFileExtension = defaultFileExtension;
        this.tocItems = new ArrayList<>();
    }

    public void addTocItem(String dirName, String fileNameWithoutExtension) {
        tocItems.add(new TocItem(dirName, fileNameWithoutExtension));
    }

    public void addTocItem(String dirName, String fileNameWithoutExtension, String sectionTitle) {
        tocItems.add(new TocItem(dirName, fileNameWithoutExtension, sectionTitle));
    }

    public void addIndex() {
        tocItems.add(0, TocItem.createIndex());
    }

    public TocItem getIndex() {
        if (tocItems.isEmpty()) {
            return null;
        }

        TocItem first = tocItems.get(0);
        return first.isIndex() ? first : null;
    }

    public TocItem tocItemByPath(Path path) {
        if (path.getFileName().toString().startsWith(TocItem.INDEX + ".")) {
            return getIndex();
        }

        return getTocItems().stream().filter(ti ->
                path.toAbsolutePath().getParent().getFileName().toString().equals(ti.getDirName()) &&
                        path.getFileName().toString().equals(
                                ti.getFileNameWithoutExtension() + "." + defaultFileExtension))
                .findFirst().orElse(null);
    }

    public List<TocItem> getTocItems() {
        return Collections.unmodifiableList(tocItems);
    }

    public boolean contains(String dirName, String fileName, String pageSectionId) {
        TocItem tocItem = findTocItem(dirName, fileName);

        return tocItem != null &&
                (pageSectionId.isEmpty() || tocItem.hasPageSection(pageSectionId));
    }

    public TocItem findTocItem(String dirName, String fileName) {
        return getTocItems().stream().filter(ti ->
                    ti.getDirName().equals(dirName) && ti.getFileNameWithoutExtension().equals(fileName))
                    .findFirst()
                    .orElse(null);
    }

    public List<Map<String, Object>> toListOfMaps() {
        Map<String, List<TocItem>> bySectionTitle = tocItems.stream().collect(
                groupingBy(TocItem::getSectionTitle, LinkedHashMap::new, toList()));

        List<Map<String, Object>> result = new ArrayList<>();
        bySectionTitle.forEach((sectionTitle, items) -> result.add(createSectionWithItems(sectionTitle, items)));

        return result;
    }

    private Map<String, Object> createSectionWithItems(String sectionTitle, List<TocItem> items) {
        Map<String, Object> result = new LinkedHashMap<>();

        if (items.isEmpty()) {
            return result;
        }

        result.put("sectionTitle", sectionTitle);
        result.put("dirName", items.iterator().next().getDirName());
        result.put("items", items.stream().map(TocItem::toMap).collect(toList()));

        return result;
    }

    @Override
    public String toString() {
        return tocItems.stream().map(TocItem::toString).collect(joining("\n"));
    }
}
