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

package org.testingisdocumenting.znai.java.parser.html;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.ParserHandlersList;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.docelement.DocElementCreationParserHandler;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;
import org.testingisdocumenting.znai.search.SearchCrawlerParserHandler;

import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class HtmlToDocElementConverter {
    public static class Result {
        private final List<DocElement> docElements;
        private final String searchText;

        public Result(List<DocElement> docElements, String searchText) {
            this.docElements = docElements;
            this.searchText = searchText;
        }

        public List<DocElement> getDocElements() {
            return docElements;
        }

        public String getSearchText() {
            return searchText;
        }
    }

    private HtmlToDocElementConverter() {
    }

    /**
     * converts html to doc elements, so it can be re-used inside generated documentation
     *
     * @param componentsRegistry components like code parser
     * @param filePath           path to use by markup parser to handle include plugins resource location
     * @param html               html to parse
     * @param codeReferences     code references to use during conversion
     * @param isMarkdown         treat text as markdown
     * @return doc element that is a representation of th®e html
     */
    public static Result convert(ComponentsRegistry componentsRegistry, Path filePath, String html, DocReferences codeReferences, boolean isMarkdown) {
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));

        SearchCrawlerParserHandler searchCrawler = new SearchCrawlerParserHandler();
        DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(componentsRegistry, filePath);
        ParserHandlersList parserHandlersList = new ParserHandlersList(parserHandler, searchCrawler);

        document.body().traverse(new ConverterNodeVisitor(filePath,
                componentsRegistry.markdownParser(),
                parserHandlersList,
                codeReferences,
                isMarkdown));

        parserHandlersList.onParsingEnd();

        return new Result(parserHandler.getDocElement().getContent(),
                searchCrawler.getSearchEntries().stream().map(se -> se.getSearchText().getText())
                        .collect(joining(" ")));
    }

    private static class ConverterNodeVisitor implements NodeVisitor {
        private final Path filePath;
        private final MarkdownParser markdownParser;
        private final ParserHandler parserHandler;
        private final DocReferences codeReferences;
        private final boolean isMarkdown;

        private boolean isInsideInlinedCode;
        private boolean isInsideBlockCode;

        /**
         * we assume an implicit paragraph start. Paragraph needs to be closed if another one starts.
         * Since we started first one implicitly there won't be any explicit node close.
         */
        private boolean isInsideParagraph;

        ConverterNodeVisitor(Path filePath,
                             MarkdownParser markdownParser,
                             ParserHandler parserHandler,
                             DocReferences codeReferences,
                             boolean isMarkdown) {
            this.filePath = filePath;
            this.markdownParser = markdownParser;
            this.parserHandler = parserHandler;
            this.codeReferences = codeReferences;
            this.isMarkdown = isMarkdown;

            if (!isMarkdown) {
                startParagraphIfRequired();
            }
        }

        @Override
        public void head(Node node, int i) {
            if (isParagraph(node)) {
                closeParagraphIfRequired();
                startParagraph();
            } else if (isInlinedCode(node)) {
                isInsideInlinedCode = true;
            } else if (isBlockCode(node)) {
                isInsideBlockCode = true;
            } else if (isBold(node)) {
                parserHandler.onStrongEmphasisStart();
            } else if (isItalic(node)) {
                parserHandler.onEmphasisStart();
            } else if (isLink(node)) {
                parserHandler.onLinkStart(node.attr("href"));
            } else if (isUnorderedList(node)) {
                closeParagraphIfRequired();
                parserHandler.onBulletListStart('*', false);
            } else if (isOrderedList(node)) {
                closeParagraphIfRequired();
                parserHandler.onOrderedListStart('.', 1);
            } else if (isListItem(node)) {
                parserHandler.onListItemStart();
            } else if (node.nodeName().equals("#text")) {
                TextNode textNode = (TextNode) node;
                handleText(textNode);
            }
        }

        @Override
        public void tail(Node node, int i) {
            if (isParagraph(node)) {
                closeParagraphIfRequired();
            } else if (isInlinedCode(node)) {
                isInsideInlinedCode = false;
            } else if (isBlockCode(node)) {
                isInsideBlockCode = false;
            } else if (isBold(node)) {
                parserHandler.onStrongEmphasisEnd();
            } else if (isItalic(node)) {
                parserHandler.onEmphasisEnd();
            } else if (isLink(node)) {
                parserHandler.onLinkEnd();
            } else if (isUnorderedList(node)) {
                parserHandler.onBulletListEnd();

                closeParagraphIfRequired();
                startParagraph();
            } else if (isOrderedList(node)) {
                parserHandler.onOrderedListEnd();

                closeParagraphIfRequired();
                startParagraph();
            } else if (isListItem(node)) {
                parserHandler.onListItemEnd();
            }
        }

        private void handleText(TextNode textNode) {
            String content = textNode.text();
            if (content.trim().isEmpty()) {
                return;
            }

            if (isInsideInlinedCode) {
                parserHandler.onInlinedCode(content, codeReferences);
            } else if (isInsideBlockCode) {
                closeParagraphIfRequired();
                parserHandler.onSnippet(PluginParams.EMPTY,"", "", content);
            } else {
                if (isMarkdown) {
                    parseMarkdownText(textNode.getWholeText());
                } else {
                    parserHandler.onSimpleText(content);
                }
            }
        }

        private void parseMarkdownText(String text) {
            markdownParser.parse(filePath, parserHandler, text.trim());
        }

        private void startParagraphIfRequired() {
            if (isInsideParagraph) {
                return;
            }

            this.startParagraph();
        }

        private void startParagraph() {
            isInsideParagraph = true;
            parserHandler.onParagraphStart();
        }

        private void closeParagraphIfRequired() {
            if (isInsideParagraph) {
                parserHandler.onParagraphEnd();
                isInsideParagraph = false;
            }
        }

        private static boolean isParagraph(Node node) {
            return node.nodeName().equals("p");
        }

        private static boolean isBlockCode(Node node) {
            return node.nodeName().equals("pre");
        }

        private static boolean isUnorderedList(Node node) {
            return node.nodeName().equals("ul");
        }

        private static boolean isOrderedList(Node node) {
            return node.nodeName().equals("ol");
        }

        private static boolean isListItem(Node node) {
            return node.nodeName().equals("li");
        }

        private static boolean isBold(Node node) {
            return node.nodeName().equals("b");
        }

        private static boolean isItalic(Node node) {
            return node.nodeName().equals("i");
        }

        private static boolean isInlinedCode(Node node) {
            return node.nodeName().equals("code");
        }

        private static boolean isLink(Node node) {
            return node.nodeName().equals("a");
        }
    }
}
