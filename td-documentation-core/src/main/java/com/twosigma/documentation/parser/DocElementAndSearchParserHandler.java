package com.twosigma.documentation.parser;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.parser.docelement.DocElementCreationParserHandler;
import com.twosigma.documentation.parser.table.MarkupTableData;

import java.nio.file.Path;
import java.util.List;

/**
 * @author mykola
 */
public class DocElementAndSearchParserHandler implements ParserHandler {
    private DocElementCreationParserHandler docElCreationHandler;
    private ParserHandler another;

    public DocElementAndSearchParserHandler(ComponentsRegistry componentsRegistry, Path path) {
        docElCreationHandler = new DocElementCreationParserHandler(componentsRegistry, path);
    }

    @Override
    public void onSectionStart(String title) {
        docElCreationHandler.onSectionStart(title);
    }

    @Override
    public void onSectionEnd() {
        docElCreationHandler.onSectionEnd();
    }

    @Override
    public void onSubHeadingStart(int level) {
        docElCreationHandler.onSubHeadingStart(level);
    }

    @Override
    public void onSubHeadingEnd(int level) {
        docElCreationHandler.onSubHeadingEnd(level);
    }

    @Override
    public void onHardLineBreak() {
        docElCreationHandler.onHardLineBreak();
    }

    @Override
    public void onSoftLineBreak() {
        docElCreationHandler.onSoftLineBreak();
    }

    @Override
    public void onParagraphStart() {
        docElCreationHandler.onParagraphStart();
    }

    @Override
    public void onParagraphEnd() {
        docElCreationHandler.onParagraphEnd();
    }

    @Override
    public void onBulletListStart(char bulletMarker, boolean tight) {
        docElCreationHandler.onBulletListStart(bulletMarker, tight);
    }

    @Override
    public void onBulletListEnd() {
        docElCreationHandler.onBulletListEnd();
    }

    @Override
    public void onOrderedListStart(char delimiter, int startNumber) {
        docElCreationHandler.onOrderedListStart(delimiter, startNumber);
    }

    @Override
    public void onOrderedListEnd() {
        docElCreationHandler.onOrderedListEnd();
    }

    @Override
    public void onListItemStart() {
        docElCreationHandler.onListItemStart();
    }

    @Override
    public void onListItemEnd() {
        docElCreationHandler.onListItemEnd();
    }

    @Override
    public void onTable(MarkupTableData tableData) {
        docElCreationHandler.onTable(tableData);
    }

    @Override
    public void onEmphasisStart() {
        docElCreationHandler.onEmphasisStart();
    }

    @Override
    public void onEmphasisEnd() {
        docElCreationHandler.onEmphasisEnd();
    }

    @Override
    public void onStrongEmphasisStart() {
        docElCreationHandler.onStrongEmphasisStart();
    }

    @Override
    public void onStrongEmphasisEnd() {
        docElCreationHandler.onStrongEmphasisEnd();
    }

    @Override
    public void onBlockQuoteStart() {
        docElCreationHandler.onBlockQuoteStart();
    }

    @Override
    public void onBlockQuoteEnd() {
        docElCreationHandler.onBlockQuoteEnd();
    }

    @Override
    public void onSimpleText(String value) {
        docElCreationHandler.onSimpleText(value);
    }

    @Override
    public void onInlinedCode(String inlinedCode) {
        docElCreationHandler.onInlinedCode(inlinedCode);
    }

    @Override
    public void onLinkStart(String url) {
        docElCreationHandler.onLinkStart(url);
    }

    @Override
    public void onLinkEnd() {
        docElCreationHandler.onLinkEnd();
    }

    @Override
    public void onImage(String title, String destination, String alt) {
        docElCreationHandler.onImage(title, destination, alt);
    }

    @Override
    public void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet) {
        docElCreationHandler.onSnippet(pluginParams, lang, lineNumber, snippet);
    }

    @Override
    public void onThematicBreak() {
        docElCreationHandler.onThematicBreak();
    }

    @Override
    public void onIncludePlugin(PluginParams pluginParams) {
        docElCreationHandler.onIncludePlugin(pluginParams);
    }

    @Override
    public void onFencePlugin(PluginParams pluginParams, String content) {
        docElCreationHandler.onFencePlugin(pluginParams, content);
    }

    @Override
    public void onInlinedCodePlugin(PluginParams pluginParams) {
        docElCreationHandler.onInlinedCodePlugin(pluginParams);
    }

    @Override
    public void onParsingEnd() {
        docElCreationHandler.onParsingEnd();
    }
}
