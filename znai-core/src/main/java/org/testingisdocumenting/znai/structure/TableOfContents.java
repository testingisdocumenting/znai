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

package org.testingisdocumenting.znai.structure;

import java.util.*;

import static java.util.stream.Collectors.*;

public class TableOfContents {
    private List<TocItem> tocItems;

    public TableOfContents() {
        this.tocItems = new ArrayList<>();
    }

    public TocItem addTocItem(String dirName, String fileNameWithoutExtension) {
        TocItem tocItem = new TocItem(dirName, fileNameWithoutExtension);
        tocItems.add(tocItem);

        return tocItem;
    }

    public TocItem addTocItem(String dirName, String fileNameWithoutExtension, String sectionTitle) {
        TocItem tocItem = new TocItem(dirName, fileNameWithoutExtension, sectionTitle);
        tocItems.add(tocItem);

        return tocItem;
    }

    public void addIndex() {
        tocItems.add(0, TocItem.createIndex());
    }

    public void removeTocItem(String dirName, String fileNameWithoutExtension) {
        tocItems.removeIf(item -> item.match(dirName, fileNameWithoutExtension));
    }

    public void replaceTocItem(String originalDirName, String originalFileNameWithoutExtension,
                               String newDirName, String newFileNameWithoutExtension) {
        int idx = findTocItemIdx(originalDirName, originalFileNameWithoutExtension);
        if (idx == -1) {
            throw new IllegalArgumentException("can't find toc item: " +
                    originalDirName + "/" + originalFileNameWithoutExtension);
        }

        tocItems.set(idx, new TocItem(newDirName, newFileNameWithoutExtension));
    }

    public List<TocItem> detectNewTocItems(TableOfContents newToc) {
        Set<TocItem> existingItems = new LinkedHashSet<>(this.tocItems);
        Set<TocItem> newItems = new LinkedHashSet<>(newToc.tocItems);

        newItems.removeAll(existingItems);
        return new ArrayList<>(newItems);
    }

    public List<TocItem> detectRemovedTocItems(TableOfContents newToc) {
        Set<TocItem> existingItems = new LinkedHashSet<>(this.tocItems);
        Set<TocItem> newItems = new LinkedHashSet<>(newToc.tocItems);

        existingItems.removeAll(newItems);
        return new ArrayList<>(existingItems);
    }

    public TocItem getIndex() {
        if (tocItems.isEmpty()) {
            return null;
        }

        TocItem first = tocItems.get(0);
        return first.isIndex() ? first : null;
    }

    public List<TocItem> getTocItems() {
        return Collections.unmodifiableList(tocItems);
    }

    public boolean contains(String dirName, String fileName, String pageSectionId) {
        TocItem tocItem = findTocItem(dirName, fileName);

        return tocItem != null &&
                (pageSectionId.isEmpty() || tocItem.hasPageSection(pageSectionId));
    }

    public TocItem findTocItem(String dirName, String fileNameWithoutExtension) {
        int idx = findTocItemIdx(dirName, fileNameWithoutExtension);
        return idx == -1 ? null : tocItems.get(idx);
    }

    public List<Map<String, Object>> toListOfMaps() {
        Map<String, List<TocItem>> bySectionTitle = tocItems.stream().collect(
                groupingBy(TocItem::getSectionTitle, LinkedHashMap::new, toList()));

        List<Map<String, Object>> result = new ArrayList<>();
        bySectionTitle.forEach((sectionTitle, items) -> result.add(createSectionWithItems(sectionTitle, items)));

        return result;
    }

    private int findTocItemIdx(String dirName, String fileNameWithoutExtension) {
        for (int idx = 0; idx < tocItems.size(); idx++) {
            if (tocItems.get(idx).match(dirName, fileNameWithoutExtension)) {
                return idx;
            }
        }

        return -1;
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
