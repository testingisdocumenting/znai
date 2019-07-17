package com.twosigma.znai.search;

import com.twosigma.znai.structure.TocItem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PageSearchEntries {
    private final TocItem tocItem;
    private List<PageSearchEntry> entries;

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
                tocItem.getSectionTitle(),
                tocItem.getPageTitle(),
                pageSearchEntry.getPageSectionTitle(),
                pageSearchEntry.getSearchText().getText());
    }

    private String genId(PageSearchEntry pageSearchEntry) {
        return tocItem.getDirName() + "@@" + tocItem.getFileNameWithoutExtension() + "@@" + pageSearchEntry.getPageSectionId();
    }
}
