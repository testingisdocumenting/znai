package com.twosigma.documentation.search;

import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.table.MarkupTableData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
public class SearchCrawlerParserHandler implements ParserHandler {
    private String docTitle;
    private String sectionTitle;
    private String pageTitle;

    private List<SearchEntry> searchEntries;
    private String pageSectionTitle;
    private List<String> currentTextParts;

    public SearchCrawlerParserHandler(String docTitle, String sectionTitle, String pageTitle) {
        this.docTitle = docTitle;
        this.sectionTitle = sectionTitle;
        this.pageTitle = pageTitle;
        this.searchEntries = new ArrayList<>();
        this.pageSectionTitle = "";
        this.currentTextParts = new ArrayList<>();
    }

    public List<SearchEntry> getSearchEntries() {
        return searchEntries;
    }

    @Override
    public void onSectionStart(String title) {
        pageSectionTitle = title;
    }

    @Override
    public void onSectionEnd() {
        searchEntries.add(createSearchEntry(SearchScore.STANDARD.text(String.join(" ", currentTextParts))));
        currentTextParts.clear();
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
        tableData.columnNamesStream().forEach(currentTextParts::add);
        tableData.allValuesStream().forEach(v -> currentTextParts.add(v.toString()));
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
    public void onIncludePlugin(PluginParams pluginParams) {

    }

    @Override
    public void onFencePlugin(PluginParams pluginParams, String content) {

    }

    @Override
    public void onInlinedCodePlugin(PluginParams pluginParams) {

    }

    @Override
    public void onParsingEnd() {

    }

    private SearchEntry createSearchEntry(SearchText searchText) {
        SearchEntry entry = new SearchEntry();
        entry.setDocTitle(docTitle);
        entry.setSectionTitle(sectionTitle);
        entry.setPageTitle(pageTitle);
        entry.setPageSectionTitle(pageSectionTitle);
        entry.setText(searchText);

        return entry;
    }
}
