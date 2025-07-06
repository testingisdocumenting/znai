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
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.reference.DocReferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MarkdownGeneratorParserHandler implements ParserHandler {
    private final StringBuilder markdownBuilder;
    private final List<String> currentParagraph;
    private int baseHeadingLevel;
    private boolean inList;
    private boolean inCodeBlock;

    public MarkdownGeneratorParserHandler(int baseHeadingLevel) {
        this.markdownBuilder = new StringBuilder();
        this.currentParagraph = new ArrayList<>();
        this.baseHeadingLevel = baseHeadingLevel;
        this.inList = false;
        this.inCodeBlock = false;
    }

    public String getMarkdown() {
        flush();
        return markdownBuilder.toString();
    }

    @Override
    public void onSectionStart(String title, HeadingProps headingProps, Heading heading) {
        flush();
        int level = Math.max(1, heading.getLevel() + baseHeadingLevel);
        markdownBuilder.append("#".repeat(level)).append(" ").append(title).append("\n\n");
    }

    @Override
    public void onSubHeading(int level, String title, HeadingProps headingProps, Heading heading) {
        flush();
        int adjustedLevel = Math.max(1, level + baseHeadingLevel);
        markdownBuilder.append("#".repeat(adjustedLevel)).append(" ").append(title).append("\n\n");
    }

    @Override
    public void onSimpleText(String value) {
        currentParagraph.add(value);
    }

    @Override
    public void onParagraphEnd() {
        flush();
        markdownBuilder.append("\n");
    }

    @Override
    public void onInlinedCode(String inlinedCode, DocReferences docReferences) {
        currentParagraph.add("`");
        currentParagraph.add(inlinedCode);
        currentParagraph.add("`");
    }

    @Override
    public void onImage(String title, String destination, String alt) {
        flush();
        markdownBuilder.append("![").append(alt != null ? alt : "").append("](").append(destination);
        if (title != null && !title.trim().isEmpty()) {
            markdownBuilder.append(" \"").append(title).append("\"");
        }
        markdownBuilder.append(")\n\n");
    }

    @Override
    public void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet) {
        flush();
        markdownBuilder.append("```").append(lang != null ? lang : "").append("\n");
        markdownBuilder.append(snippet);
        if (!snippet.endsWith("\n")) {
            markdownBuilder.append("\n");
        }
        markdownBuilder.append("```\n\n");
    }

    @Override
    public void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult) {
        flush();
        // For markdown generation, we'll represent plugins as comments
        markdownBuilder.append("<!-- Include Plugin: ").append(includePlugin.id()).append(" -->\n\n");
    }

    @Override
    public void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult) {
        flush();
        // For markdown generation, we'll represent plugins as comments
        markdownBuilder.append("<!-- Fence Plugin: ").append(fencePlugin.id()).append(" -->\n\n");
    }

    @Override
    public void onTable(MarkupTableData tableData) {
        flush();
        markdownBuilder.append(convertTableToMarkdown(tableData)).append("\n\n");
    }

    @Override
    public void onFootnoteDefinition(ParsedFootnote footnote) {
        flush();
        markdownBuilder.append(convertFootnoteToMarkdown(footnote)).append("\n\n");
    }

    @Override
    public void onSoftLineBreak() {
        currentParagraph.add(" ");
    }

    @Override
    public void onHardLineBreak() {
        currentParagraph.add("  \n");
    }

    @Override
    public void onListItemStart() {
        flush();
        inList = true;
    }

    @Override
    public void onListItemEnd() {
        flush();
        markdownBuilder.append("\n");
        inList = false;
    }

    @Override
    public void onOrderedListStart(char delimiter, int startNumber) {
        flush();
    }

    @Override
    public void onOrderedListEnd() {
        flush();
        markdownBuilder.append("\n");
    }

    @Override
    public void onHtml(String html, boolean isInlined) {
        String plainText = Jsoup.parse(html).text();
        if (!plainText.isEmpty()) {
            if (isInlined) {
                currentParagraph.add(plainText);
            } else {
                flush();
                markdownBuilder.append(plainText).append("\n\n");
            }
        }
    }

    @Override
    public void onParsingEnd() {
        flush();
    }

    @Override
    public void onSectionEnd() {
        // No specific action needed for section end in markdown generation
    }

    @Override
    public void onParagraphStart() {
        // No specific action needed for paragraph start
    }

    @Override
    public void onEmphasisStart() {
        currentParagraph.add("*");
    }

    @Override
    public void onEmphasisEnd() {
        currentParagraph.add("*");
    }

    @Override
    public void onStrongEmphasisStart() {
        currentParagraph.add("**");
    }

    @Override
    public void onStrongEmphasisEnd() {
        currentParagraph.add("**");
    }

    @Override
    public void onStrikeThroughStart() {
        currentParagraph.add("~~");
    }

    @Override
    public void onStrikeThroughEnd() {
        currentParagraph.add("~~");
    }

    @Override
    public void onBlockQuoteStart() {
        flush();
        markdownBuilder.append("> ");
    }

    @Override
    public void onBlockQuoteEnd() {
        flush();
        markdownBuilder.append("\n");
    }

    @Override
    public void onLinkStart(String url) {
        currentParagraph.add("[");
    }

    @Override
    public void onLinkEnd() {
        // Note: This is a simplified implementation
        // A complete implementation would need to track the URL from onLinkStart
        currentParagraph.add("]()");
    }

    @Override
    public void onThematicBreak() {
        flush();
        markdownBuilder.append("---\n\n");
    }

    @Override
    public void onCustomNodeStart(String nodeName, Map<String, ?> attrs) {
        // Custom nodes are typically plugin-specific, so we skip them
    }

    @Override
    public void onCustomNode(String nodeName, Map<String, ?> attrs) {
        // Custom nodes are typically plugin-specific, so we skip them
    }

    @Override
    public void onCustomNodeEnd(String nodeName) {
        // Custom nodes are typically plugin-specific, so we skip them
    }

    @Override
    public void onDocElement(DocElement docElement) {
        // DocElements are typically handled by other handlers
    }

    @Override
    public void onGlobalAnchor(String id) {
        // Anchors don't have direct markdown representation
    }

    @Override
    public void onGlobalAnchorRefStart(String id) {
        // Anchor references don't have direct markdown representation
    }

    @Override
    public void onGlobalAnchorRefEnd() {
        // Anchor references don't have direct markdown representation
    }

    @Override
    public void onFootnoteReference(FootnoteId footnoteId) {
        currentParagraph.add("[^" + footnoteId.id() + "]");
    }

    @Override
    public void onInlinedCodePlugin(InlinedCodePlugin inlinedCodePlugin, PluginResult pluginResult) {
        // For markdown generation, we'll represent plugins as comments
        currentParagraph.add("<!-- Inlined Code Plugin: " + inlinedCodePlugin.id() + " -->");
    }

    @Override
    public void onBulletListStart(char bulletMarker, boolean tight) {
        flush();
    }

    @Override
    public void onBulletListEnd() {
        flush();
        markdownBuilder.append("\n");
    }

    private void flush() {
        if (currentParagraph.isEmpty()) {
            return;
        }

        String paragraphText = String.join("", currentParagraph).trim();
        if (!paragraphText.isEmpty()) {
            if (inList) {
                markdownBuilder.append("- ").append(paragraphText);
            } else {
                markdownBuilder.append(paragraphText);
            }
            
            if (!inList) {
                markdownBuilder.append("\n");
            }
        }

        currentParagraph.clear();
    }

    private String convertTableToMarkdown(MarkupTableData tableData) {
        StringBuilder table = new StringBuilder();
        List<String> columnTitles = tableData.getColumnTitles();
        List<List<Object>> data = tableData.getData();

        if (columnTitles.isEmpty()) {
            return "";
        }

        // Header row
        table.append("| ").append(String.join(" | ", columnTitles)).append(" |\n");
        
        // Separator row
        table.append("|").append(" --- |".repeat(columnTitles.size())).append("\n");
        
        // Data rows
        for (List<Object> row : data) {
            table.append("| ");
            for (int i = 0; i < columnTitles.size(); i++) {
                String cell = i < row.size() && row.get(i) != null ? row.get(i).toString() : "";
                table.append(cell.replace("|", "\\|"));
                if (i < columnTitles.size() - 1) {
                    table.append(" | ");
                }
            }
            table.append(" |\n");
        }

        return table.toString();
    }

    private String convertFootnoteToMarkdown(ParsedFootnote footnote) {
        return "[^" + footnote.id().id() + "]: " + footnote.allText();
    }
}