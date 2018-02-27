package com.twosigma.documentation.search;

import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.table.MarkupTableData;

import java.util.Map;

public class NoOpParserHandler implements ParserHandler {
    @Override
    public void onSectionStart(String title) {
    }

    @Override
    public void onSectionEnd() {
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
    }

    @Override
    public void onInlinedCode(String inlinedCode) {
    }

    @Override
    public void onLinkStart(String url) {
    }

    @Override
    public void onLinkEnd() {
    }

    @Override
    public void onImage(String title, String destination, String alt) {
    }

    @Override
    public void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet) {
    }

    @Override
    public void onThematicBreak() {
    }

    @Override
    public void onCustomNodeStart(String nodeName, Map<String, ?> attrs) {
    }

    @Override
    public void onCustomNodeEnd(String nodeName) {
    }

    @Override
    public void onGlobalAnchor(String id) {
    }

    @Override
    public void onGlobalAnchorRefStart(String id) {
    }

    @Override
    public void onGlobalAnchorRefEnd() {
    }

    @Override
    public void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult) {
    }

    @Override
    public void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult) {
    }

    @Override
    public void onInlinedCodePlugin(PluginParams pluginParams) {
    }

    @Override
    public void onParsingEnd() {
    }
}
