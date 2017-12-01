package com.twosigma.documentation.parser;

import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.table.MarkupTableData;

import java.util.Arrays;
import java.util.List;

/**
 * @author mykola
 */
public class ParserHandlersList implements ParserHandler {
    private List<ParserHandler> list;

    public ParserHandlersList(ParserHandler... parsers) {
        list = Arrays.asList(parsers);
    }

    @Override
    public void onSectionStart(String title) {
        list.forEach(h -> h.onSectionStart(title));
    }

    @Override
    public void onSectionEnd() {
        list.forEach(ParserHandler::onSectionEnd);
    }

    @Override
    public void onSubHeadingStart(int level) {
        list.forEach(h -> h.onSubHeadingStart(level));
    }

    @Override
    public void onSubHeadingEnd(int level) {
        list.forEach(h -> h.onSubHeadingEnd(level));
    }

    @Override
    public void onHardLineBreak() {
        list.forEach(ParserHandler::onHardLineBreak);
    }

    @Override
    public void onSoftLineBreak() {
        list.forEach(ParserHandler::onSoftLineBreak);
    }

    @Override
    public void onParagraphStart() {
        list.forEach(ParserHandler::onParagraphStart);
    }

    @Override
    public void onParagraphEnd() {
        list.forEach(ParserHandler::onParagraphEnd);
    }

    @Override
    public void onBulletListStart(char bulletMarker, boolean tight) {
        list.forEach(h -> h.onBulletListStart(bulletMarker, tight));
    }

    @Override
    public void onBulletListEnd() {
        list.forEach(ParserHandler::onBulletListEnd);
    }

    @Override
    public void onOrderedListStart(char delimiter, int startNumber) {
        list.forEach(h -> h.onOrderedListStart(delimiter, startNumber));
    }

    @Override
    public void onOrderedListEnd() {
        list.forEach(ParserHandler::onOrderedListEnd);
    }

    @Override
    public void onListItemStart() {
        list.forEach(ParserHandler::onListItemStart);
    }

    @Override
    public void onListItemEnd() {
        list.forEach(ParserHandler::onListItemEnd);
    }

    @Override
    public void onTable(MarkupTableData tableData) {
        list.forEach(h -> h.onTable(tableData));
    }

    @Override
    public void onEmphasisStart() {
        list.forEach(ParserHandler::onEmphasisStart);
    }

    @Override
    public void onEmphasisEnd() {
        list.forEach(ParserHandler::onEmphasisEnd);
    }

    @Override
    public void onStrongEmphasisStart() {
        list.forEach(ParserHandler::onStrongEmphasisStart);
    }

    @Override
    public void onStrongEmphasisEnd() {
        list.forEach(ParserHandler::onStrongEmphasisEnd);
    }

    @Override
    public void onBlockQuoteStart() {
        list.forEach(ParserHandler::onBlockQuoteStart);
    }

    @Override
    public void onBlockQuoteEnd() {
        list.forEach(ParserHandler::onBlockQuoteEnd);
    }

    @Override
    public void onSimpleText(String value) {
        list.forEach(h -> h.onSimpleText(value));
    }

    @Override
    public void onInlinedCode(String inlinedCode) {
        list.forEach(h -> h.onInlinedCode(inlinedCode));
    }

    @Override
    public void onLinkStart(String url) {
        list.forEach(h -> h.onLinkStart(url));
    }

    @Override
    public void onLinkEnd() {
        list.forEach(ParserHandler::onLinkEnd);
    }

    @Override
    public void onImage(String title, String destination, String alt) {
        list.forEach(h -> h.onImage(title, destination, alt));
    }

    @Override
    public void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet) {
        list.forEach(h -> h.onSnippet(pluginParams, lang, lineNumber, snippet));
    }

    @Override
    public void onThematicBreak() {
        list.forEach(ParserHandler::onThematicBreak);
    }

    @Override
    public void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult) {
        list.forEach(h -> h.onIncludePlugin(includePlugin, pluginResult));
    }

    @Override
    public void onFencePlugin(PluginParams pluginParams, String content) {
        list.forEach(h -> h.onFencePlugin(pluginParams, content));
    }

    @Override
    public void onInlinedCodePlugin(PluginParams pluginParams) {
        list.forEach(h -> h.onInlinedCodePlugin(pluginParams));
    }

    @Override
    public void onParsingEnd() {
        list.forEach(ParserHandler::onParsingEnd);
    }
}
