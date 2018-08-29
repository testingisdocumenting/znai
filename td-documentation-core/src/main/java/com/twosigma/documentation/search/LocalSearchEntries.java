package com.twosigma.documentation.search;

import com.twosigma.utils.JsonUtils;
import com.twosigma.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocalSearchEntries {
    private List<PageSearchEntries> entries;

    public LocalSearchEntries() {
        this.entries = new ArrayList<>();
    }

    public void add(PageSearchEntries entries) {
        this.entries.add(entries);
    }

    public List<List<String>> toListOfLists() {
        return entries.stream()
                .flatMap(e -> e.toListOfLists().stream())
                .collect(Collectors.toList());
    }

    public String buildIndexScript() {
        return "mdocSearchData = " + JsonUtils.serialize(toListOfLists()) + "\n" +
                ResourceUtils.textContent("lunrjs/indexCreation.js");
    }
}
