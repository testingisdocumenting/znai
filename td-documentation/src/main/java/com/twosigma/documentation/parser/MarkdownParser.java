package com.twosigma.documentation.parser;

import java.util.Collections;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;

import com.twosigma.documentation.parser.commonmark.CommonMarkExtension;
import com.twosigma.documentation.parser.commonmark.include.IncludeNode;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.parser.docelement.DocElementCreationParserHandler;

/**
 * @author mykola
 */
public class MarkdownParser implements MarkupParser {
    private final Parser parser;

    public MarkdownParser() {
        final CommonMarkExtension extension = new CommonMarkExtension();
        parser = Parser.builder().extensions(Collections.singletonList(extension)).build();
    }

    public DocElement parse(String markdown) {
        Node node = parser.parse(markdown);

        final DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler();
        final DocElementVisitor visitor = new DocElementVisitor(parserHandler);
        node.accept(visitor);

        if (visitor.sectionStarted) {
            parserHandler.onSectionEnd();
        }

        return parserHandler.getDocElement();
    }

    private static class DocElementVisitor extends AbstractVisitor {
        private DocElementCreationParserHandler parserHandler;
        private boolean sectionStarted;

        public DocElementVisitor(DocElementCreationParserHandler parserHandler) {
            this.parserHandler = parserHandler;
        }

        @Override
        public void visit(final Paragraph paragraph) {
            parserHandler.onParagraphStart();
            visitChildren(paragraph);
            parserHandler.onParagraphEnd();
        }

        @Override
        public void visit(final Emphasis emphasis) {
            parserHandler.onEmphasisStart();
            visitChildren(emphasis);
            parserHandler.onEmphasisEnd();
        }

        @Override
        public void visit(final StrongEmphasis strongEmphasis) {
            parserHandler.onStrongEmphasisStart();
            visitChildren(strongEmphasis);
            parserHandler.onStrongEmphasisEnd();
        }

        @Override
        public void visit(final Text text) {
            parserHandler.onSimpleText(text.getLiteral());
        }

        @Override
        public void visit(BulletList bulletList) {
            parserHandler.onBulletListStart(bulletList.getBulletMarker(), bulletList.isTight());
            visitChildren(bulletList);
            parserHandler.onBulletListEnd();
        }

        @Override
        public void visit(ListItem listItem) {
            parserHandler.onListItemStart();
            visitChildren(listItem);
            parserHandler.onListItemEnd();
        }

        @Override
        public void visit(final CustomBlock customBlock) {
            if (customBlock instanceof IncludeNode) {
                final IncludeNode includeNode = (IncludeNode) customBlock;
                parserHandler.onInclude(includeNode.getId(), includeNode.getValue());
            } else {
                super.visit(customBlock);
            }
        }

        @Override
        public void visit(final IndentedCodeBlock indentedCodeBlock) {
            parserHandler.onSnippet("", "", indentedCodeBlock.getLiteral());
        }

        @Override
        public void visit(final Link link) {
            parserHandler.onLink(link.getTitle(), link.getDestination());
        }

        @Override
        public void visit(final Heading heading) {
            if (heading.getLevel() == 1) {
                if (sectionStarted) {
                    parserHandler.onSectionEnd();
                }

                final String literal = ((Text) heading.getFirstChild()).getLiteral();
                parserHandler.onSectionStart(literal);
                sectionStarted = true;
            } else {
                super.visit(heading);
            }
        }
    }
}
