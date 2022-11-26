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
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.commonmark.include.IncludeBlock;
import org.testingisdocumenting.znai.parser.table.GfmTableToTableConverter;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.commonmark.ext.front.matter.YamlFrontMatterBlock;
import org.commonmark.ext.gfm.strikethrough.Strikethrough;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.node.*;
import org.testingisdocumenting.znai.utils.JsonParseException;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Path;
import java.util.Map;

public class MarkdownVisitor extends AbstractVisitor {
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

        if (idAndParams != null) {
            handleInlineCodePlugin(new PluginParams(idAndParams.getId(), idAndParams.getParams()));
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
        parserHandler.onSnippet(PluginParams.EMPTY, "", "", indentedCodeBlock.getLiteral());
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        PluginParams pluginParams = extractFencePluginParams(fencedCodeBlock.getInfo().trim());
        if (Plugins.hasFencePlugin(pluginParams.getPluginId())) {
            handleFencePlugin(pluginParams, fencedCodeBlock.getLiteral());
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
        HeadingTextAndProps headingTextAndProps = extractHeadingTextAndProps(heading);

        if (heading.getLevel() == 1) {
            if (sectionStarted) {
                parserHandler.onSectionEnd();
            }

            parserHandler.onSectionStart(headingTextAndProps.text, headingTextAndProps.props);
            sectionStarted = true;
        } else {
            parserHandler.onSubHeading(heading.getLevel(), headingTextAndProps.text, headingTextAndProps.props);
        }
    }

    private void handleIncludePlugin(PluginParams params) {
        try {
            IncludePlugin includePlugin = Plugins.includePluginById(params.getPluginId());
            includePlugin.preprocess(componentsRegistry, path, params);

            includePlugin.parameters().validate(params);

            PluginResult pluginResult = includePlugin.process(componentsRegistry, parserHandler, path, params);

            parserHandler.onIncludePlugin(includePlugin, pluginResult);
        } catch (Exception e) {
            throw new RuntimeException(createPluginErrorMessage("include", params, e), e);
        }
    }

    private void handleFencePlugin(PluginParams params, String fenceContent) {
        try {
            FencePlugin fencePlugin = Plugins.fencePluginById(params.getPluginId());
            fencePlugin.preprocess(componentsRegistry, path, params);
            fencePlugin.preprocess(componentsRegistry, path, params, fenceContent);

            fencePlugin.parameters().validate(params);

            PluginResult pluginResult = fencePlugin.process(componentsRegistry, path, params, fenceContent);

            parserHandler.onFencePlugin(fencePlugin, pluginResult);
        } catch (Exception e) {
            throw new RuntimeException(createPluginErrorMessage("fence", params, e) + "\n" +
                    "  fence content:\n" + fenceContent, e);
        }
    }

    private void handleInlineCodePlugin(PluginParams params) {
        try {
            InlinedCodePlugin inlinedCodePlugin = Plugins.inlinedCodePluginById(params.getPluginId());
            inlinedCodePlugin.preprocess(componentsRegistry, path, params);
            inlinedCodePlugin.parameters().validate(params);

            PluginResult pluginResult = inlinedCodePlugin.process(componentsRegistry, path, params);
            parserHandler.onInlinedCodePlugin(inlinedCodePlugin, pluginResult);
        } catch (Exception e) {
            throw new RuntimeException(createPluginErrorMessage("inline code", params, e), e);
        }
    }

    private static String createPluginErrorMessage(String pluginType, PluginParams params, Exception e) {
        return "error handling " + pluginType + " plugin <" + params.getPluginId() + ">\n" +
                "  free param: " + params.getFreeParam() + "\n" +
                "  opts: " + JsonUtils.serialize(params.getOpts().toMap()) + "\n\n" + e.getMessage() + "\n";
    }

    private static PluginParams extractFencePluginParams(String nameAndParams) {
        int firstSpaceIdx = nameAndParams.indexOf(' ');
        return (firstSpaceIdx == -1) ?
                new PluginParams(nameAndParams, ""):
                new PluginParams(nameAndParams.substring(0, firstSpaceIdx),
                        nameAndParams.substring(firstSpaceIdx + 1));

    }

    private HeadingTextAndProps extractHeadingTextAndProps(Heading heading) {
        heading.accept(ValidateNoExtraSyntaxExceptInlineCodeInHeadingVisitor.INSTANCE);
        Node firstChild = heading.getFirstChild();

        if (firstChild == null) {
            return new HeadingTextAndProps("", HeadingProps.EMPTY);
        }

        String extractedText = extractText(firstChild);

        int startOfCurlyIdx = extractedText.indexOf('{');
        if (startOfCurlyIdx == -1) {
            return new HeadingTextAndProps(extractedText, HeadingProps.EMPTY);
        }


        try {
            String jsonStart = extractedText.substring(startOfCurlyIdx);

            Map<String, ?> props = JsonUtils.deserializeAsMap(jsonStart);
            String headingTextOnly = extractedText.substring(0, startOfCurlyIdx).trim();
            return new HeadingTextAndProps(headingTextOnly, new HeadingProps(props));
        } catch (JsonParseException e) {
            throw new RuntimeException("Can't parse props of heading: " + extractedText, e);
        }
    }

    private String extractText(Node node) {
        if (node == null) {
            return "";
        }

        return ((Text) node).getLiteral().trim();
    }

    private static class HeadingTextAndProps {
        private final String text;
        private final HeadingProps props;

        public HeadingTextAndProps(String text, HeadingProps props) {
            this.text = text;
            this.props = props;
        }
    }
}
