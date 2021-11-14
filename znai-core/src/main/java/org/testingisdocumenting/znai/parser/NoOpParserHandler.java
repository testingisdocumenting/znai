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

package org.testingisdocumenting.znai.parser;

import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.reference.DocReferences;

import java.util.Map;

public class NoOpParserHandler implements ParserHandler {
    @Override
    public void onSectionStart(String title, HeadingProps headingProps) {
    }

    @Override
    public void onSectionEnd() {
    }

    @Override
    public void onSubHeading(int level, String title, HeadingProps headingProps) {
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
    public void onStrikeThroughStart() {
    }

    @Override
    public void onStrikeThroughEnd() {
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
    public void onInlinedCode(String inlinedCode, DocReferences docReferences) {
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
    public void onCustomNode(String nodeName, Map<String, ?> attrs) {
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
    public void onInlinedCodePlugin(InlinedCodePlugin inlinedCodePlugin, PluginResult pluginResult) {
    }

    @Override
    public void onParsingEnd() {
    }
}
