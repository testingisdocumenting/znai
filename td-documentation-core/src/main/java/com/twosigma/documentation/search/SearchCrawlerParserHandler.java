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
public class SearchCrawlerParserHandler implements ParserHandler {
    private List<PageSearchEntry> searchEntries;
    private String pageSectionId;
    private String pageSectionTitle;
    private List<String> currentTextParts;

    public SearchCrawlerParserHandler() {
        this.searchEntries = new ArrayList<>();
        this.pageSectionId = "";
        this.pageSectionTitle = "";
        this.currentTextParts = new ArrayList<>();
    }

    public List<PageSearchEntry> getSearchEntries() {
        return searchEntries;
    }

    @Override
    public void onSectionStart(String title) {
        pageSectionId =
        pageSectionTitle = title;
    }

    @Override
    public void onSectionEnd() {
        flushTextParts();
    }

    @Override
    public void onSubHeadingStart(int level) {
    }

    @Override
    public void onSubHeadingEnd(int level) {

    }

    @Override
    public void onHardLineBreak() {

    }

    @Override
    public void onSoftLineBreak() {

    }

    @Override
    public void onParagraphStart() {

    }

    @Override
    public void onParagraphEnd() {

    }

    @Override
    public void onBulletListStart(char bulletMarker, boolean tight) {

    }

    @Override
    public void onBulletListEnd() {

    }

    @Override
    public void onOrderedListStart(char delimiter, int startNumber) {

    }

    @Override
    public void onOrderedListEnd() {

    }

    @Override
    public void onListItemStart() {

    }

    @Override
    public void onListItemEnd() {

    }

    @Override
    public void onTable(MarkupTableData tableData) {
        currentTextParts.add(tableData.allText());
    }

    @Override
    public void onEmphasisStart() {

    }

    @Override
    public void onEmphasisEnd() {

    }

    @Override
    public void onStrongEmphasisStart() {

    }

    @Override
    public void onStrongEmphasisEnd() {

    }

    @Override
    public void onBlockQuoteStart() {

    }

    @Override
    public void onBlockQuoteEnd() {

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
    public void onLinkEnd() {

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
    public void onThematicBreak() {

    }

    @Override
    public void onGlobalAnchor(String id) {

    }

    @Override
    public void onGlobalAnchorRef(String id, String label) {

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
    public void onInlinedCodePlugin(PluginParams pluginParams) {

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
