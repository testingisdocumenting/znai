package com.twosigma.documentation.search;

import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.table.MarkupTableData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
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
        currentTextParts.add(tableData.allText());
    }

    @Override
    public void onSimpleText(String value) {
        currentTextParts.add(value);
    }

    @Override
    public void onInlinedCode(String inlinedCode) {
        currentTextParts.add(inlinedCode);
    }

    @Override
    public void onLinkStart(String url) {
        currentTextParts.add(url);
    }

    @Override
    public void onImage(String title, String destination, String alt) {
        currentTextParts.add(title);
        currentTextParts.add(alt);
    }

    @Override
    public void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet) {
        currentTextParts.add(snippet);
    }

    @Override
    public void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult) {
        SearchText searchText = includePlugin.textForSearch();
        if (searchText != null) {
            currentTextParts.add(searchText.getText());
        }
    }

    @Override
    public void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult) {
        SearchText searchText = fencePlugin.textForSearch();
        if (searchText != null) {
            currentTextParts.add(searchText.getText());
        }
    }

    @Override
    public void onParsingEnd() {
        flushTextParts();
    }

    private void flushTextParts() {
        if (currentTextParts.isEmpty()) {
            return;
        }

        searchEntries.add(new PageSearchEntry(pageSectionTitle,
                SearchScore.STANDARD.text(String.join(" ", currentTextParts))));
        currentTextParts.clear();
    }
}
