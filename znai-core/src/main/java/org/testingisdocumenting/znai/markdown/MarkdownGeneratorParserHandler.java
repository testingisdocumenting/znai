/*
 * Copyright 2025 znai maintainers
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

package org.testingisdocumenting.znai.markdown;

import org.commonmark.node.Heading;
import org.jsoup.Jsoup;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.extensions.footnote.FootnoteId;
import org.testingisdocumenting.znai.extensions.footnote.ParsedFootnote;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.reference.DocReferences;

import java.util.List;
import java.util.Map;

public class MarkdownGeneratorParserHandler implements ParserHandler {
    enum State {
        INSIDE_LIST,
        DEFAULT
    }

    private final StringBuilder markdown;
    private final int baseHeadingLevel;
    private State state = State.DEFAULT;

    public MarkdownGeneratorParserHandler(int baseHeadingLevel) {
        this.markdown = new StringBuilder();
        this.baseHeadingLevel = baseHeadingLevel;
    }

    public String getMarkdown() {
        return markdown.toString().replaceAll("\n\n\n", "\n\n");
    }

    @Override
    public void onSectionStart(String title, HeadingProps headingProps, Heading heading) {
        if (heading == null) {
            return;
        }

        int level = Math.max(1, heading.getLevel() + baseHeadingLevel);
        markdown.append("#".repeat(level)).append(" ").append(title).append("\n\n");
    }

    @Override
    public void onSubHeading(int level, String title, HeadingProps headingProps, Heading heading) {
        int adjustedLevel = Math.max(1, level + baseHeadingLevel);
        markdown.append("#".repeat(adjustedLevel)).append(" ").append(title).append("\n\n");
    }

    @Override
    public void onSimpleText(String value) {
        markdown.append(value);
    }

    @Override
    public void onParagraphStart() {
    }

    @Override
    public void onParagraphEnd() {
        switch (state) {
            case INSIDE_LIST -> markdown.append("\n");
            case DEFAULT -> markdown.append("\n\n");
        }
    }

    @Override
    public void onInlinedCode(String inlinedCode, DocReferences docReferences) {
        markdown.append("`").append(inlinedCode).append("`");
    }

    @Override
    public void onEmphasisStart() {
        markdown.append("*");
    }

    @Override
    public void onEmphasisEnd() {
        markdown.append("*");
    }

    @Override
    public void onStrongEmphasisStart() {
        markdown.append("**");
    }

    @Override
    public void onStrongEmphasisEnd() {
        markdown.append("**");
    }

    @Override
    public void onStrikeThroughStart() {
        markdown.append("~~");
    }

    @Override
    public void onStrikeThroughEnd() {
        markdown.append("~~");
    }

    @Override
    public void onListItemStart() {
        markdown.append("- ");
    }

    @Override
    public void onListItemEnd() {
    }

    @Override
    public void onBulletListStart(char bulletMarker, boolean tight) {
        state = State.INSIDE_LIST;
    }

    @Override
    public void onBulletListEnd() {
        state = State.DEFAULT;
        markdown.append("\n");
    }

    @Override
    public void onOrderedListStart(char delimiter, int startNumber) {
        state = State.INSIDE_LIST;
    }

    @Override
    public void onOrderedListEnd() {
        state = State.DEFAULT;
        markdown.append("\n");
    }

    @Override
    public void onBlockQuoteStart() {
        markdown.append("> ");
    }

    @Override
    public void onBlockQuoteEnd() {
        markdown.append("\n");
    }

    @Override
    public void onImage(String title, String destination, String alt) {
        markdown.append("![").append(alt != null ? alt : "").append("](").append(destination);
        if (title != null && !title.trim().isEmpty()) {
            markdown.append(" \"").append(title).append("\"");
        }
        markdown.append(")\n\n");
    }

    @Override
    public void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet) {
        markdown.append("```").append(lang != null ? lang : "").append("\n");
        markdown.append(snippet);
        if (!snippet.endsWith("\n")) {
            markdown.append("\n");
        }
        markdown.append("```\n\n");
    }

    @Override
    public void onTable(MarkupTableData tableData) {
        List<String> columnTitles = tableData.getColumnTitles();
        List<List<Object>> data = tableData.getData();

        if (columnTitles.isEmpty()) {
            return;
        }

        // Header row
        markdown.append("| ").append(String.join(" | ", columnTitles)).append(" |\n");
        
        // Separator row
        markdown.append("|").append(" --- |".repeat(columnTitles.size())).append("\n");
        
        // Data rows
        for (List<Object> row : data) {
            markdown.append("| ");
            for (int i = 0; i < columnTitles.size(); i++) {
                String cell = i < row.size() && row.get(i) != null ? row.get(i).toString() : "";
                markdown.append(cell.replace("|", "\\|"));
                if (i < columnTitles.size() - 1) {
                    markdown.append(" | ");
                }
            }
            markdown.append(" |\n");
        }
        markdown.append("\n\n");
    }

    @Override
    public void onFootnoteDefinition(ParsedFootnote footnote) {
        markdown.append("[^").append(footnote.id().id()).append("]: ").append(footnote.allText()).append("\n\n");
    }

    @Override
    public void onSoftLineBreak() {
        markdown.append(" ");
    }

    @Override
    public void onHardLineBreak() {
        markdown.append("  \n");
    }

    @Override
    public void onThematicBreak() {
        markdown.append("---\n\n");
    }

    @Override
    public void onHtml(String html, boolean isInlined) {
        String plainText = Jsoup.parse(html).text();
        if (!plainText.isEmpty()) {
            markdown.append(plainText);
            if (!isInlined) {
                markdown.append("\n\n");
            }
        }
    }

    @Override
    public void onParsingEnd() {
    }

    @Override
    public void onSectionEnd() {
    }

    @Override
    public void onLinkStart(String url) {
        markdown.append("[");
    }

    @Override
    public void onLinkEnd() {
        markdown.append("]()");
    }

    @Override
    public void onFootnoteReference(FootnoteId footnoteId) {
        markdown.append("[^").append(footnoteId.id()).append("]");
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
    public void onDocElement(DocElement docElement) {
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
        String markdownRepresentation = includePlugin.markdownRepresentation();
        markdown.append(markdownRepresentation).append("\n\n");
    }

    @Override
    public void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult) {
        String markdownRepresentation = fencePlugin.markdownRepresentation();
        markdown.append(markdownRepresentation).append("\n\n");
    }

    @Override
    public void onInlinedCodePlugin(InlinedCodePlugin inlinedCodePlugin, PluginResult pluginResult) {
        String markdownRepresentation = inlinedCodePlugin.markdownRepresentation();
        markdown.append(markdownRepresentation);
    }
}