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

package org.testingisdocumenting.znai.search;

import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.NoOpParserHandler;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.reference.DocReferences;

import java.util.ArrayList;
import java.util.List;

public class SearchCrawlerParserHandler extends NoOpParserHandler {
    private final List<PageSearchEntry> searchEntries;
    private String pageSectionTitle;
    private final List<String> standardScoreParts;
    private final List<String> highScoreParts;

    public SearchCrawlerParserHandler() {
        this.searchEntries = new ArrayList<>();
        this.pageSectionTitle = "";
        this.standardScoreParts = new ArrayList<>();
        this.highScoreParts = new ArrayList<>();
    }

    public List<PageSearchEntry> getSearchEntries() {
        return searchEntries;
    }

    @Override
    public void onSectionStart(String title, HeadingProps headingProps) {
        pageSectionTitle = title;
    }

    @Override
    public void onSectionEnd() {
        flush();
    }

    @Override
    public void onSubHeading(int level, String title, HeadingProps headingProps) {
        addStandardWithSpaceSeparator(title);
    }

    @Override
    public void onTable(MarkupTableData tableData) {
        addStandardWithSpaceSeparator(tableData.allText());
    }

    @Override
    public void onSimpleText(String value) {
        addStandard(replaceCommonSeparatorsWithSpace(value));
    }

    @Override
    public void onParagraphEnd() {
        addStandard(" ");
    }

    @Override
    public void onInlinedCode(String inlinedCode, DocReferences docReferences) {
        addHighWithSpaceSeparator(replaceCommonSeparatorsWithSpace(inlinedCode));
    }

    @Override
    public void onImage(String title, String destination, String alt) {
        addStandardWithSpaceSeparator(title);
        addStandardWithSpaceSeparator(alt);
    }

    @Override
    public void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet) {
        addHighWithSpaceSeparator(snippet);
    }

    @Override
    public void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult) {
        processPluginSearchText(includePlugin.textForSearch());
    }

    @Override
    public void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult) {
        processPluginSearchText(fencePlugin.textForSearch());
    }

    @Override
    public void onSoftLineBreak() {
        addStandard(" ");
    }

    @Override
    public void onHardLineBreak() {
        addStandard(" ");
    }

    @Override
    public void onListItemEnd() {
        addStandard(" ");
    }

    @Override
    public void onParsingEnd() {
        flush();
    }

    private void flush() {
        flushTextParts();
    }

    private void processPluginSearchText(SearchText searchText) {
        if (searchText == null) {
            return;
        }

        switch (searchText.getScore()) {
            case HIGH -> addHighWithSpaceSeparator(searchText.getText());
            case STANDARD -> addStandardWithSpaceSeparator(searchText.getText());
        }
    }

    private void addStandard(String part) {
        standardScoreParts.add(part);
    }

    private void addStandardWithSpaceSeparator(String part) {
        if (part == null) {
            return;
        }

        standardScoreParts.add(' ' + replaceCommonSeparatorsWithSpace(part) + ' ');
    }

    private void addHighWithSpaceSeparator(String part) {
        if (part == null) {
            return;
        }

        highScoreParts.add(' ' + replaceCommonSeparatorsWithSpace(part) + ' ');
    }

    private String replaceCommonSeparatorsWithSpace(String text) {
        return text.replaceAll("[.,();:\\-+=\\\\/\"'!?\\[\\]{}]", " ");
    }

    private SearchText createSearchText(SearchScore score, List<String> parts) {
        return new SearchText(score,
                String.join("", parts)
                        .replaceAll("\\s+", " ")
                        .trim());
    }
    private void flushTextParts() {
        if (standardScoreParts.isEmpty() && highScoreParts.isEmpty()) {
            return;
        }

        searchEntries.add(new PageSearchEntry(pageSectionTitle, List.of(
                createSearchText(SearchScore.STANDARD, standardScoreParts),
                createSearchText(SearchScore.HIGH, highScoreParts))));
    }
}
