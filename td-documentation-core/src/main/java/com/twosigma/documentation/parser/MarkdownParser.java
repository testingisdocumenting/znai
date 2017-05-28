package com.twosigma.documentation.parser;

import java.nio.file.Path;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.Plugins;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;

import com.twosigma.documentation.parser.commonmark.CommonMarkExtension;
import com.twosigma.documentation.parser.commonmark.include.IncludeNode;
import com.twosigma.documentation.parser.docelement.DocElementCreationParserHandler;

/**
 * @author mykola
 */
public class MarkdownParser implements MarkupParser {
    private final Parser parser;
    private final ComponentsRegistry componentsRegistry;

    private static final Pattern INLINED_CODE_ID_PATTERN = Pattern.compile("^([a-zA-Z-_]+):(.*)");

    public MarkdownParser(ComponentsRegistry componentsRegistry) {
        this.componentsRegistry = componentsRegistry;
        CommonMarkExtension extension = new CommonMarkExtension();
        parser = Parser.builder().extensions(Collections.singletonList(extension)).build();
    }

    public MarkupParserResult parse(Path path, String markdown) {
        Node node = parser.parse(markdown);

        final DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(componentsRegistry, path);
        final DocElementVisitor visitor = new DocElementVisitor(parserHandler);
        node.accept(visitor);

        if (visitor.sectionStarted) {
            parserHandler.onSectionEnd();
        }

        parserHandler.onParsingEnd();

        return new MarkupParserResult(parserHandler.getDocElement(), parserHandler.getAuxiliaryFiles());
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
        public void visit(OrderedList orderedList) {
            parserHandler.onOrderedListStart(orderedList.getDelimiter(), orderedList.getStartNumber());
            visitChildren(orderedList);
            parserHandler.onOrderedListEnd();
        }

        @Override
        public void visit(ListItem listItem) {
            parserHandler.onListItemStart();
            visitChildren(listItem);
            parserHandler.onListItemEnd();
        }

        @Override
        public void visit(Code code) {
            String literal = code.getLiteral();
            Matcher matcher = INLINED_CODE_ID_PATTERN.matcher(literal);
            if (matcher.matches() && Plugins.hasInlinedCodePlugin(matcher.group(1))) {
                parserHandler.onInlinedCodePlugin(new PluginParams(matcher.group(1), matcher.group(2)));
            } else {
                parserHandler.onInlinedCode(literal);
            }
        }

        @Override
        public void visit(ThematicBreak thematicBreak) {
            parserHandler.onThematicBreak();
        }

        @Override
        public void visit(HardLineBreak hardLineBreak) {
            parserHandler.onHardLineBreak();
        }

        @Override
        public void visit(SoftLineBreak softLineBreak) {
            parserHandler.onSoftLineBreak();
        }

        @Override
        public void visit(BlockQuote blockQuote) {
            parserHandler.onBlockQuoteStart();
            visitChildren(blockQuote);
            parserHandler.onBlockQuoteEnd();
        }

        @Override
        public void visit(CustomBlock customBlock) {
            if (customBlock instanceof IncludeNode) {
                final IncludeNode includeNode = (IncludeNode) customBlock;
                parserHandler.onIncludePlugin(includeNode.getParams());
            } else {
                super.visit(customBlock);
            }
        }

        @Override
        public void visit(Image image) {
            Node firstChild = image.getFirstChild();
            parserHandler.onImage(image.getTitle(), image.getDestination(), ((Text) firstChild).getLiteral());
        }

        @Override
        public void visit(final IndentedCodeBlock indentedCodeBlock) {
            parserHandler.onSnippet("", "", indentedCodeBlock.getLiteral());
        }

        @Override
        public void visit(final FencedCodeBlock fencedCodeBlock) {
            PluginParams pluginParams = extractFencePluginParams(fencedCodeBlock.getInfo().trim());
            if (Plugins.hasFencePlugin(pluginParams.getPluginId())) {
                parserHandler.onFencePlugin(pluginParams, fencedCodeBlock.getLiteral());
            } else {
                parserHandler.onSnippet(fencedCodeBlock.getInfo(), "", fencedCodeBlock.getLiteral());
            }
        }

        private static PluginParams extractFencePluginParams(String nameAndParams) {
            int firstSpaceIdx = nameAndParams.indexOf(' ');
            return (firstSpaceIdx == -1) ?
                    new PluginParams(nameAndParams, ""):
                    new PluginParams(nameAndParams.substring(0, firstSpaceIdx),
                            nameAndParams.substring(firstSpaceIdx + 1));

        }

        @Override
        public void visit(final Link link) {
            parserHandler.onLinkStart(link.getDestination());
            visitChildren(link);
            parserHandler.onLinkEnd();
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
