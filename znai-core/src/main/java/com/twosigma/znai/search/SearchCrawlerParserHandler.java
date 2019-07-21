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

package com.twosigma.znai.search;

import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.NoOpParserHandler;
import com.twosigma.znai.parser.table.MarkupTableData;

import java.util.ArrayList;
import java.util.List;

public class SearchCrawlerParserHandler extends NoOpParserHandler {
    private List<PageSearchEntry> searchEntries;
    private String pageSectionTitle;
    private List<String> currentTextParts;

    public SearchCrawlerParserHandler() {
        this.searchEntries = new ArrayList<>();
        this.pageSectionTitle = "";
        this.currentTextParts = new ArrayList<>();
    }

    public List<PageSearchEntry> getSearchEntries() {
        return searchEntries;
    }

    @Override
    public void onSectionStart(String title) {
        pageSectionTitle = title;
    }

    @Override
    public void onSectionEnd() {
        flushTextParts();
    }

    @Override
    public void onTable(MarkupTableData tableData) {
        addSeparated(tableData.allText());
    }

    @Override
    public void onSimpleText(String value) {
        add(value);
    }

    @Override
    public void onInlinedCode(String inlinedCode) {
        addSeparated(inlinedCode);
    }

    @Override
    public void onLinkStart(String url) {
        addSeparated(url);
    }

    @Override
    public void onImage(String title, String destination, String alt) {
        addSeparated(title);
        addSeparated(destination);
        addSeparated(alt);
    }

    @Override
    public void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet) {
        addSeparated(lang);
        addSeparated(snippet);
    }

    @Override
    public void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult) {
        SearchText searchText = includePlugin.textForSearch();
        if (searchText != null) {
            addSeparated(searchText.getText());
        }
    }

    @Override
    public void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult) {
        SearchText searchText = fencePlugin.textForSearch();
        if (searchText != null) {
            addSeparated(searchText.getText());
        }
    }

    @Override
    public void onSoftLineBreak() {
        add(" ");
    }

    @Override
    public void onHardLineBreak() {
        add(" ");
    }

    @Override
    public void onParsingEnd() {
        flushTextParts();
    }

    private void add(String part) {
        currentTextParts.add(part);
    }

    private void addSeparated(String part) {
        currentTextParts.add(' ' + part + ' ');
    }

    private void flushTextParts() {
        if (currentTextParts.isEmpty()) {
            return;
        }

        SearchText searchText = SearchScore.STANDARD.text(
                String.join("", currentTextParts)
                        .replaceAll("\\s+", " ")
                        .trim());

        searchEntries.add(new PageSearchEntry(pageSectionTitle, searchText));
        currentTextParts.clear();
    }
}
