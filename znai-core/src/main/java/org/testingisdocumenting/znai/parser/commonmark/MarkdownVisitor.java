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

package org.testingisdocumenting.znai.parser.commonmark;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.Plugins;
import org.testingisdocumenting.znai.extensions.PluginsRegexp;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.commonmark.include.IncludeBlock;
import org.testingisdocumenting.znai.parser.table.GfmTableToTableConverter;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.commonmark.ext.front.matter.YamlFrontMatterBlock;
import org.commonmark.ext.gfm.strikethrough.Strikethrough;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.node.*;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownVisitor extends AbstractVisitor {
    private static final Pattern INLINED_CODE_ID_PATTERN = Pattern.compile("^:([a-zA-Z-_]+):\\s*(.*)");

    private final ComponentsRegistry componentsRegistry;
    private final Path path;
    private final ParserHandler parserHandler;
    private boolean sectionStarted;

    public MarkdownVisitor(ComponentsRegistry componentsRegistry, Path path, ParserHandler parserHandler) {
        this.componentsRegistry = componentsRegistry;
        this.path = path;
        this.parserHandler = parserHandler;
    }

    public boolean isSectionStarted() {
        return sectionStarted;
    }

    @Override
    public void visit(Paragraph paragraph) {
        parserHandler.onParagraphStart();
        visitChildren(paragraph);
        parserHandler.onParagraphEnd();
    }

    @Override
    public void visit(Emphasis emphasis) {
        parserHandler.onEmphasisStart();
        visitChildren(emphasis);
        parserHandler.onEmphasisEnd();
    }

    @Override
    public void visit(StrongEmphasis strongEmphasis) {
        parserHandler.onStrongEmphasisStart();
        visitChildren(strongEmphasis);
        parserHandler.onStrongEmphasisEnd();
    }

    @Override
    public void visit(Text text) {
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
        PluginsRegexp.IdAndParams idAndParams = PluginsRegexp.parseInlinedCodePlugin(literal);

        if (idAndParams != null && Plugins.hasInlinedCodePlugin(idAndParams.getId())) {
            parserHandler.onInlinedCodePlugin(new PluginParams(idAndParams.getId(), idAndParams.getParams()));
        } else {
            parserHandler.onInlinedCode(literal, DocReferences.EMPTY);
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
        if (customBlock instanceof YamlFrontMatterBlock) {
            return;
        }

        if (customBlock instanceof IncludeBlock) {
            final IncludeBlock includeBlock = (IncludeBlock) customBlock;
            handleIncludePlugin(includeBlock.getParams());
        } else if (customBlock instanceof TableBlock) {
            GfmTableToTableConverter gfmTableToTableConverter = new GfmTableToTableConverter(componentsRegistry, path, (TableBlock) customBlock);
            parserHandler.onTable(gfmTableToTableConverter.convert());
        } else {
            throw new UnsupportedOperationException("unsupported custom block: " + customBlock);
        }
    }

    @Override
    public void visit(CustomNode customNode) {
        if (customNode instanceof Strikethrough) {
            parserHandler.onStrikeThroughStart();
            visitChildren(customNode);
            parserHandler.onStrikeThroughEnd();
        } else {
            super.visit(customNode);
        }
    }

    @Override
    public void visit(Image image) {
        Node firstChild = image.getFirstChild();
        String alt = extractText(firstChild);
        parserHandler.onImage(image.getTitle(), image.getDestination(), alt.isEmpty() ? "image" : alt);
    }

    @Override
    public void visit(IndentedCodeBlock indentedCodeBlock) {
        parserHandler.onSnippet(PluginParams.EMPTY,"", "", indentedCodeBlock.getLiteral());
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        PluginParams pluginParams = extractFencePluginParams(fencedCodeBlock.getInfo().trim());
        if (Plugins.hasFencePlugin(pluginParams.getPluginId())) {
            FencePlugin fencePlugin = Plugins.fencePluginById(pluginParams.getPluginId());
            PluginResult pluginResult = fencePlugin.process(componentsRegistry, path, pluginParams, fencedCodeBlock.getLiteral());

            parserHandler.onFencePlugin(fencePlugin, pluginResult);
        } else {
            parserHandler.onSnippet(pluginParams, pluginParams.getPluginId(), "", fencedCodeBlock.getLiteral());
        }
    }

    @Override
    public void visit(Link link) {
        parserHandler.onLinkStart(link.getDestination());
        visitChildren(link);
        parserHandler.onLinkEnd();
    }

    @Override
    public void visit(Heading heading) {
        if (heading.getLevel() == 1) {
            if (sectionStarted) {
                parserHandler.onSectionEnd();
            }

            parserHandler.onSectionStart(extractHeadingText(heading));
            sectionStarted = true;
        } else {
            parserHandler.onSubHeading(heading.getLevel(), extractHeadingText(heading));
        }
    }

    private void handleIncludePlugin(PluginParams params) {
        IncludePlugin includePlugin = Plugins.includePluginById(params.getPluginId());
        PluginResult pluginResult = includePlugin.process(componentsRegistry, parserHandler, path, params);

        parserHandler.onIncludePlugin(includePlugin, pluginResult);
    }

    private static PluginParams extractFencePluginParams(String nameAndParams) {
        int firstSpaceIdx = nameAndParams.indexOf(' ');
        return (firstSpaceIdx == -1) ?
                new PluginParams(nameAndParams, ""):
                new PluginParams(nameAndParams.substring(0, firstSpaceIdx),
                        nameAndParams.substring(firstSpaceIdx + 1));

    }

    private String extractHeadingText(Heading heading) {
        heading.accept(ValidateNoExtraSyntaxInHeadingVisitor.INSTANCE);
        Node firstChild = heading.getFirstChild();

        if (firstChild == null) {
            return "";
        }

        return extractText(firstChild);
    }

    private String extractText(Node node) {
        if (node == null) {
            return "";
        }

        return ((Text) node).getLiteral();
    }
}
