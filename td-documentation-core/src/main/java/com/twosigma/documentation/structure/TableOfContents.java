package com.twosigma.documentation.structure;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class TableOfContents {
    private List<TocItem> tocItems;

    public TableOfContents() {
        tocItems = new ArrayList<>();
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

    public List<TocItem> getTocItems() {
        return Collections.unmodifiableList(tocItems);
    }

    public boolean contains(String dirName, String fileName, String pageSectionId) {
        TocItem tocItem = findTocItem(dirName, fileName);

        if (tocItem == null) {
            return false;
        }

        return pageSectionId.isEmpty() || tocItem.hasPageSection(pageSectionId);
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
