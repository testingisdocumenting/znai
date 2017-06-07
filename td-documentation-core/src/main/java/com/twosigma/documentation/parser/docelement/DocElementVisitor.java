package com.twosigma.documentation.parser.docelement;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.Plugins;
import com.twosigma.documentation.parser.commonmark.include.IncludeNode;
import com.twosigma.documentation.parser.table.GfmTableToTableConverter;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.node.*;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mykola
 */
public class DocElementVisitor extends AbstractVisitor {
    private static final Pattern INLINED_CODE_ID_PATTERN = Pattern.compile("^([a-zA-Z-_]+):(.*)");

    private final ComponentsRegistry componentsRegistry;
    private final Path path;
    private DocElementCreationParserHandler parserHandler;
    private boolean sectionStarted;

    public DocElementVisitor(ComponentsRegistry componentsRegistry, Path path, DocElementCreationParserHandler parserHandler) {
        this.componentsRegistry = componentsRegistry;
        this.path = path;
        this.parserHandler = parserHandler;
    }

    public boolean isSectionStarted() {
        return sectionStarted;
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
        } else if (customBlock instanceof TableBlock) {
            GfmTableToTableConverter gfmTableToTableConverter = new GfmTableToTableConverter(componentsRegistry, path, (TableBlock) customBlock);
            parserHandler.onTable(gfmTableToTableConverter.convert());
        } else {
            throw new UnsupportedOperationException("unsupported custom block: " + customBlock);
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
            parserHandler.onSubHeadingStart(heading.getLevel());
            visitChildren(heading);
            parserHandler.onSubHeadingEnd(heading.getLevel());
        }
    }
}
