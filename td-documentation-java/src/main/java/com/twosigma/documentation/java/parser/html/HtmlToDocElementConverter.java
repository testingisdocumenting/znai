package com.twosigma.documentation.java.parser.html;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.parser.docelement.DocElementCreationParserHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

import java.nio.file.Path;
import java.util.List;

/**
 * @author mykola
 */
public class HtmlToDocElementConverter {
    private HtmlToDocElementConverter() {
    }

    /**
     * converts html to doc elements so it can be re-used inside generated documentation
     *
     * @param componentsRegistry components like code parser
     * @param filePath           path to use by markup parser to handle include plugins resource location
     * @param html               html to parse
     * @return doc element that is a representation of th®e html
     */
    public static List<DocElement> convert(ComponentsRegistry componentsRegistry, Path filePath, String html) {
        Document document = Jsoup.parse(html);

        DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(componentsRegistry, filePath);
        document.body().traverse(new ConverterNodeVisitor(parserHandler));

        parserHandler.onParsingEnd();

        return parserHandler.getDocElement().getContent();
    }

    private static class ConverterNodeVisitor implements NodeVisitor {
        private ParserHandler parserHandler;
        private boolean isInsideInlinedCode;
        private boolean isInsideBlockCode;

        /**
         * we assume an implicit paragraph start. Paragraph needs to be closed if another one starts.
         * Since we started first one implicitly there won't be any explicit node close.
         */
        private boolean isInsideParagraph;

        ConverterNodeVisitor(ParserHandler parserHandler) {
            this.parserHandler = parserHandler;
            startParagraphIfRequired();
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
                handleText(textNode.text());
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

        private void handleText(String text) {
            if (text.trim().isEmpty()) {
                return;
            }

            if (isInsideInlinedCode) {
                parserHandler.onInlinedCode(text);
            } else if (isInsideBlockCode) {
                closeParagraphIfRequired();
                parserHandler.onSnippet(PluginParams.EMPTY,"", "", text);
            } else {
                parserHandler.onSimpleText(text);
            }
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
