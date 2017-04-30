package com.twosigma.documentation.java.parser.html;

import com.twosigma.documentation.core.ComponentsRegistry;
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
     * @param filePath path to use by markup parser to handle include plugins resource location
     * @param html html to parse
     * @return doc element that is a representation of th®e html
     */
    public static List<DocElement> convert(ComponentsRegistry componentsRegistry, Path filePath, String html) {
        Document document = Jsoup.parse(html);

        DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(componentsRegistry, filePath);
        document.body().traverse(new ConverterNodeVisitor(parserHandler));

        return parserHandler.getDocElement().getContent();
    }

    private static class ConverterNodeVisitor implements NodeVisitor {
        private ParserHandler parserHandler;

        /**
         * we assume an implicit paragraph start. Paragraph needs to be closed if another one starts.
         * Since we started first one implicitly there won't be any explicit node close.
         */
        private boolean isInsideParagraph;

        public ConverterNodeVisitor(ParserHandler parserHandler) {
            this.parserHandler = parserHandler;
            this.isInsideParagraph = true;
            parserHandler.onParagraphStart();
        }

        @Override
        public void head(Node node, int i) {
            if (node.nodeName().equals("#text")) {
                TextNode textNode = (TextNode) node;
                String text = textNode.text();

                if (! text.trim().isEmpty()) {
                    parserHandler.onSimpleText(text);
                }
            } else if (isBold(node)) {
                parserHandler.onStrongEmphasisStart();
            } else if (isItalic(node)) {
                parserHandler.onEmphasisStart();
            } else if (isParagraph(node)) {
                if (isInsideParagraph) {
                    parserHandler.onParagraphEnd();
                }

                parserHandler.onParagraphStart();
                isInsideParagraph = true;
            }
        }

        @Override
        public void tail(Node node, int i) {
            if (isParagraph(node)) {
                parserHandler.onParagraphEnd();
                isInsideParagraph = false;
            } else if (isBold(node)) {
                parserHandler.onStrongEmphasisEnd();
            } else if (isItalic(node)) {
                parserHandler.onEmphasisEnd();
            }
        }

        private static boolean isParagraph(Node node) {
            return node.nodeName().equals("p");
        }

        private static boolean isBold(Node node) {
            return node.nodeName().equals("b");
        }

        private static boolean isItalic(Node node) {
            return node.nodeName().equals("i");
        }
    }
}
