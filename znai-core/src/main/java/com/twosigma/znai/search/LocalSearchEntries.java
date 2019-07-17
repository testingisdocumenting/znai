package com.twosigma.znai.search;

import com.twosigma.znai.structure.TocItem;
import com.twosigma.utils.JsonUtils;
import com.twosigma.utils.ResourceUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LocalSearchEntries {
    private Map<TocItem, PageSearchEntries> entries;

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
        return "mdocSearchData = " + JsonUtils.serialize(toListOfLists()) + "\n" +
                ResourceUtils.textContent("lunrjs/indexCreation.js");
    }
}
