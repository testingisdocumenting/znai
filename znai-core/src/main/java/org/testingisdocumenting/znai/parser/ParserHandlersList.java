/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.parser;

import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.reference.DocReferences;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ParserHandlersList implements ParserHandler {
    private final List<ParserHandler> list;

    public ParserHandlersList(ParserHandler... parsers) {
        list = Arrays.asList(parsers);
    }

    @Override
    public void onSectionStart(String title, HeadingProps headingProps) {
        list.forEach(h -> h.onSectionStart(title, headingProps));
    }

    @Override
    public void onSectionEnd() {
        list.forEach(ParserHandler::onSectionEnd);
    }

    @Override
    public void onSubHeading(int level, String title, HeadingProps headingProps) {
        list.forEach(h -> h.onSubHeading(level, title, headingProps));
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
    public void onStrikeThroughStart() {
        list.forEach(ParserHandler::onStrikeThroughStart);
    }

    @Override
    public void onStrikeThroughEnd() {
        list.forEach(ParserHandler::onStrikeThroughEnd);
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
    public void onInlinedCode(String inlinedCode, DocReferences docReferences) {
        list.forEach(h -> h.onInlinedCode(inlinedCode, docReferences));
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
    public void onCustomNodeStart(String nodeName, Map<String, ?> attrs) {
        list.forEach(h -> h.onCustomNodeStart(nodeName, attrs));
    }

    @Override
    public void onCustomNode(String nodeName, Map<String, ?> attrs) {
        list.forEach(h -> h.onCustomNode(nodeName, attrs));
    }

    @Override
    public void onCustomNodeEnd(String nodeName) {
        list.forEach(h -> h.onCustomNodeEnd(nodeName));
    }

    @Override
    public void onGlobalAnchor(String id) {
        list.forEach(h -> h.onGlobalAnchor(id));
    }

    @Override
    public void onGlobalAnchorRefStart(String id) {
        list.forEach(h -> h.onGlobalAnchorRefStart(id));
    }

    @Override
    public void onGlobalAnchorRefEnd() {
        list.forEach(ParserHandler::onGlobalAnchorRefEnd);
    }

    @Override
    public void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult) {
        list.forEach(h -> h.onIncludePlugin(includePlugin, pluginResult));
    }

    @Override
    public void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult) {
        list.forEach(h -> h.onFencePlugin(fencePlugin, pluginResult));
    }

    @Override
    public void onInlinedCodePlugin(InlinedCodePlugin inlinedCodePlugin, PluginResult pluginResult) {
        list.forEach(h -> h.onInlinedCodePlugin(inlinedCodePlugin, pluginResult));
    }

    @Override
    public void onParsingEnd() {
        list.forEach(ParserHandler::onParsingEnd);
    }
}
