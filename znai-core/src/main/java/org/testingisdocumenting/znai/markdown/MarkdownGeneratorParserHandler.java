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

import org.commonmark.ext.gfm.strikethrough.Strikethrough;
import org.commonmark.node.*;
import org.jsoup.Jsoup;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.extensions.footnote.FootnoteId;
import org.testingisdocumenting.znai.extensions.footnote.ParsedFootnote;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.PageSectionIdTitle;
import org.testingisdocumenting.znai.parser.commonmark.HeadingTextAndProps;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.utils.TextLineWrapper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class MarkdownGeneratorParserHandler implements ParserHandler {
    private static final int MAX_LINE_WIDTH = 120;

    enum ListType {
        BULLET,
        ORDERED
    }

    record ListState(ListType type, char marker, int orderedNumber) {
        static ListState bullet(char marker) {
            return new ListState(ListType.BULLET, marker, 0);
        }

        static ListState ordered(int startNumber) {
            return new ListState(ListType.ORDERED, '.', startNumber);
        }

        ListState withNextNumber() {
            return new ListState(type, marker, orderedNumber + 1);
        }
    }

    private final List<PageMarkdownSection> sections;
    private final Deque<ListState> listStack = new ArrayDeque<>();

    private String currentSectionId;
    private String currentSectionTitle;
    private StringBuilder currentMarkdown;
    private StringBuilder paragraphContent;

    private String linkUrl;

    public MarkdownGeneratorParserHandler() {
        this.sections = new ArrayList<>();

        this.currentSectionId = "";
        this.currentSectionTitle = "";
        this.currentMarkdown = new StringBuilder();
    }

    public PageMarkdown getMarkdown() {
        return new PageMarkdown(List.copyOf(sections));
    }

    private void finalizeCurrentSection() {
        if (!currentMarkdown.isEmpty()) {
            String markdown = currentMarkdown.toString().replaceAll("\n\n\n", "\n\n");
            sections.add(new PageMarkdownSection(currentSectionId, currentSectionTitle, markdown));
        }
    }

    @Override
    public void onSectionStart(String title, HeadingProps headingProps, Heading heading) {
        if (heading == null) {
            return;
        }

        finalizeCurrentSection();

        currentSectionId = new PageSectionIdTitle(title, headingProps.props()).getId();
        currentSectionTitle = title;
        currentMarkdown = new StringBuilder();
    }

    @Override
    public void onSubHeading(int level, String title, HeadingProps headingProps, Heading heading) {
        currentMarkdown.append("#".repeat(level)).append(" ");
        if (heading != null) {
            currentMarkdown.append(generateFormattedHeadingContent(heading));
        } else {
            currentMarkdown.append(title);
        }
        currentMarkdown.append("\n\n");
    }

    private String generateFormattedHeadingContent(Heading heading) {
        StringBuilder result = new StringBuilder();
        heading.accept(new AbstractVisitor() {
            @Override
            public void visit(Text text) {
                String textWithoutProps = HeadingTextAndProps.extractTextAndProps(text.getLiteral()).text();
                result.append(textWithoutProps);
            }

            @Override
            public void visit(Code code) {
                result.append("`").append(code.getLiteral()).append("`");
            }

            @Override
            public void visit(Emphasis emphasis) {
                result.append("*");
                visitChildren(emphasis);
                result.append("*");
            }

            @Override
            public void visit(StrongEmphasis strongEmphasis) {
                result.append("**");
                visitChildren(strongEmphasis);
                result.append("**");
            }

            @Override
            public void visit(CustomNode customNode) {
                if (customNode instanceof Strikethrough) {
                    result.append("~~");
                    visitChildren(customNode);
                    result.append("~~");
                }
            }
        });
        return result.toString().trim();
    }

    @Override
    public void onSimpleText(String value) {
        appendContentToParagraph(value);
    }

    @Override
    public void onParagraphStart() {
        paragraphContent = new StringBuilder();
    }

    @Override
    public void onParagraphEnd() {
        currentMarkdown.append(TextLineWrapper.wrapLine(paragraphContent.toString(), MAX_LINE_WIDTH));
        currentMarkdown.append(listStack.isEmpty() ? "\n\n" : "\n");
        paragraphContent = null;
    }

    @Override
    public void onInlinedCode(String inlinedCode, DocReferences docReferences) {
        appendContentToParagraph("`" + inlinedCode + "`");
    }

    @Override
    public void onEmphasisStart() {
        appendContentToParagraph("*");
    }

    @Override
    public void onEmphasisEnd() {
        appendContentToParagraph("*");
    }

    @Override
    public void onStrongEmphasisStart() {
        appendContentToParagraph("**");
    }

    @Override
    public void onStrongEmphasisEnd() {
        appendContentToParagraph("**");
    }

    @Override
    public void onStrikeThroughStart() {
        appendContentToParagraph("~~");
    }

    @Override
    public void onStrikeThroughEnd() {
        appendContentToParagraph("~~");
    }

    @Override
    public void onListItemStart() {
        if (listStack.isEmpty()) {
            return;
        }

        ListState currentList = listStack.peek();
        String indent = "  ".repeat(listStack.size() - 1);

        switch (currentList.type()) {
            case BULLET -> currentMarkdown.append(indent).append(currentList.marker()).append(" ");
            case ORDERED -> {
                currentMarkdown.append(indent).append(currentList.orderedNumber()).append(". ");
                listStack.pop();
                listStack.push(currentList.withNextNumber());
            }
        }
    }

    @Override
    public void onListItemEnd() {
    }

    @Override
    public void onBulletListStart(char bulletMarker, boolean tight) {
        listStack.push(ListState.bullet(bulletMarker));
    }

    @Override
    public void onBulletListEnd() {
        listStack.pop();
        if (listStack.isEmpty()) {
            currentMarkdown.append("\n");
        }
    }

    @Override
    public void onOrderedListStart(char delimiter, int startNumber) {
        listStack.push(ListState.ordered(startNumber));
    }

    @Override
    public void onOrderedListEnd() {
        listStack.pop();
        if (listStack.isEmpty()) {
            currentMarkdown.append("\n");
        }
    }

    @Override
    public void onBlockQuoteStart() {
        currentMarkdown.append("> ");
    }

    @Override
    public void onBlockQuoteEnd() {
        currentMarkdown.append("\n");
    }

    @Override
    public void onImage(String title, String destination, String alt) {
        currentMarkdown.append("![").append(alt != null ? alt : "").append("](").append(destination);
        if (title != null && !title.trim().isEmpty()) {
            currentMarkdown.append(" \"").append(title).append("\"");
        }
        currentMarkdown.append(")\n\n");
    }

    @Override
    public void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet) {
        currentMarkdown.append("```").append(lang != null ? lang : "").append("\n");
        currentMarkdown.append(snippet);
        if (!snippet.endsWith("\n")) {
            currentMarkdown.append("\n");
        }
        currentMarkdown.append("```\n\n");
    }

    @Override
    public void onTable(MarkupTableData tableData) {
        List<String> columnTitles = tableData.getColumnTitles();
        List<List<Object>> data = tableData.getData();

        if (columnTitles.isEmpty()) {
            return;
        }

        currentMarkdown.append("| ").append(String.join(" | ", columnTitles)).append(" |\n");
        currentMarkdown.append("|").append(" --- |".repeat(columnTitles.size())).append("\n");

        for (List<Object> row : data) {
            currentMarkdown.append("| ");
            for (int i = 0; i < columnTitles.size(); i++) {
                String cell = i < row.size() && row.get(i) != null ? row.get(i).toString() : "";
                currentMarkdown.append(cell.replace("|", "\\|"));
                if (i < columnTitles.size() - 1) {
                    currentMarkdown.append(" | ");
                }
            }
            currentMarkdown.append(" |\n");
        }
        currentMarkdown.append("\n\n");
    }

    @Override
    public void onFootnoteDefinition(ParsedFootnote footnote, int footnoteIdx) {
        currentMarkdown.append("[^").append(footnote.id().id()).append("]: ").append(footnote.allText()).append("\n\n");
    }

    @Override
    public void onSoftLineBreak() {
        appendContentToParagraph(" ");
    }

    @Override
    public void onHardLineBreak() {
        appendContentToParagraph("  \n");
    }

    @Override
    public void onThematicBreak() {
        currentMarkdown.append("---\n\n");
    }

    @Override
    public void onHtml(String html, boolean isInlined) {
        String plainText = Jsoup.parse(html).text();
        if (!plainText.isEmpty()) {
            currentMarkdown.append(plainText);
            if (!isInlined) {
                currentMarkdown.append("\n\n");
            }
        }
    }

    @Override
    public void onParsingEnd() {
        finalizeCurrentSection();
    }

    @Override
    public void onSectionEnd() {
    }

    @Override
    public void onLinkStart(String url) {
        appendContentToParagraph("[");
        linkUrl = url;
    }

    @Override
    public void onLinkEnd() {
        appendContentToParagraph("](" + (linkUrl != null ? linkUrl : "") + ")");
        linkUrl = null;
    }

    @Override
    public void onFootnoteReference(FootnoteId footnoteId) {
        appendContentToParagraph("[^" + footnoteId.id() + "]");
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
        currentMarkdown.append(markdownRepresentation).append("\n\n");
    }

    @Override
    public void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult) {
        String markdownRepresentation = fencePlugin.markdownRepresentation();
        currentMarkdown.append(markdownRepresentation).append("\n\n");
    }

    @Override
    public void onInlinedCodePlugin(InlinedCodePlugin inlinedCodePlugin, PluginResult pluginResult) {
        String markdownRepresentation = inlinedCodePlugin.markdownRepresentation();
        currentMarkdown.append(markdownRepresentation);
    }

    private void appendContentToParagraph(String value) {
        if (paragraphContent != null) {
            paragraphContent.append(value);
        } else {
            currentMarkdown.append(value);
        }
    }
}