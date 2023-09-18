/*
 * Copyright 2020 znai maintainers
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

import org.testingisdocumenting.znai.core.MarkupPathWithError;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

public class TableOfContents {
    private final List<TocItem> tocItems;
    private final Map<Path, TocItem> tocItemByPath;

    public TableOfContents() {
        this.tocItems = new ArrayList<>();
        this.tocItemByPath = new LinkedHashMap<>();
    }

    public TocItem addTocItem(TocNameAndOpts chapter, String fileNameWithoutExtension) {
        TocItem tocItem = new TocItem(chapter, fileNameWithoutExtension);
        tocItems.add(tocItem);

        return tocItem;
    }

    public TocItem addTocItem(String dirName, String fileNameWithoutExtension, String sectionTitle) {
        TocItem tocItem = new TocItem(dirName, fileNameWithoutExtension, sectionTitle);
        tocItems.add(tocItem);

        return tocItem;
    }

    public List<TocItem> resolveTocItemPathsAndReturnMissing(Function<TocItem, MarkupPathWithError> filePathResolver) {
        tocItemByPath.clear();
        List<TocItem> missing = new ArrayList<>();
        for (TocItem tocItem : tocItems) {
            if (tocItem.isIndex()) {
                continue;
            }

            MarkupPathWithError pathWithError = filePathResolver.apply(tocItem);
            if (pathWithError.error() != null) {
                missing.add(tocItem);
            } else {
                tocItemByPath.put(pathWithError.path(), tocItem);
            }
        }

        return missing;
    }

    public void addIndex() {
        tocItems.add(0, TocItem.createIndex());
    }

    public TocItem firstNonIndexPage() {
        return tocItems.stream()
                .filter(tocItem -> !tocItem.isIndex())
                .findFirst()
                .orElse(null);
    }

    public void removeTocItem(String dirName, String fileNameWithoutExtension) {
        tocItems.removeIf(item -> item.match(dirName, fileNameWithoutExtension));
    }

    public void replaceTocItem(String originalDirName, String originalFileNameWithoutExtension,
                               TocNameAndOpts newChapter, String newFileNameWithoutExtension) {
        int idx = findTocItemIdx(originalDirName, originalFileNameWithoutExtension);
        if (idx == -1) {
            throw new IllegalArgumentException("can't find toc item: " +
                    originalDirName + "/" + originalFileNameWithoutExtension);
        }

        tocItems.set(idx, new TocItem(newChapter, newFileNameWithoutExtension));
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

    public Collection<Path> getResolvedPaths() {
        return tocItemByPath.keySet();
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

    public TocItem findTocItem(Path markupFilePath) {
        return tocItemByPath.get(markupFilePath);
    }

    public List<Map<String, Object>> toListOfMaps() {
        Map<String, List<TocItem>> bySectionTitle = tocItems.stream().collect(
                groupingBy(TocItem::getChapterTitle, LinkedHashMap::new, toList()));

        List<Map<String, Object>> result = new ArrayList<>();
        bySectionTitle.forEach((sectionTitle, items) -> result.add(createChapterWithItems(sectionTitle, items)));

        return result;
    }

    @Override
    public String toString() {
        return tocItems.stream().map(TocItem::toString).collect(joining("\n"));
    }

    private int findTocItemIdx(String dirName, String fileNameWithoutExtension) {
        for (int idx = 0; idx < tocItems.size(); idx++) {
            if (tocItems.get(idx).match(dirName, fileNameWithoutExtension)) {
                return idx;
            }
        }

        return -1;
    }

    private Map<String, Object> createChapterWithItems(String sectionTitle, List<TocItem> items) {
        Map<String, Object> result = new LinkedHashMap<>();

        if (items.isEmpty()) {
            return result;
        }

        result.put("chapterTitle", sectionTitle);
        result.put("dirName", items.iterator().next().getDirName());
        result.put("items", items.stream().map(TocItem::toMap).collect(toList()));

        return result;
    }
}
