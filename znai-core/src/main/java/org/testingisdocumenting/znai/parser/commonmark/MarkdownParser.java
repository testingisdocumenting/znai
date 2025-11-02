/*
 * Copyright 2022 znai maintainers
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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.commonmark.ext.footnotes.FootnotesExtension;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.console.ansi.FontStyle;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.core.Log;
import org.testingisdocumenting.znai.extensions.PluginParamWarning;
import org.testingisdocumenting.znai.extensions.PluginParamsFactory;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.ParserHandlersList;
import org.testingisdocumenting.znai.search.SearchCrawlerParserHandler;
import org.testingisdocumenting.znai.structure.PageMeta;
import org.testingisdocumenting.znai.parser.docelement.DocElementCreationParserHandler;
import org.testingisdocumenting.znai.markdown.MarkdownGeneratorParserHandler;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;

public class MarkdownParser implements MarkupParser {
    private final Parser fullParser;
    private final Parser metaOnlyParser;
    private final ComponentsRegistry componentsRegistry;

    public MarkdownParser(ComponentsRegistry componentsRegistry) {
        this.componentsRegistry = componentsRegistry;
        fullParser = createCommonMarkParser(componentsRegistry.pluginParamsFactory());

        metaOnlyParser = Parser.builder().extensions(
                Collections.singletonList(YamlFrontMatterExtension.create())).build();
    }

    public MarkupParserResult parse(Path path, String markdown) {
        SearchCrawlerParserHandler searchCrawler = new SearchCrawlerParserHandler();
        DocElementCreationParserHandler elementCreationHandler =
                new DocElementCreationParserHandler(componentsRegistry, path);
        MarkdownGeneratorParserHandler markdownGenerator = new MarkdownGeneratorParserHandler();

        ParserHandlersList parserHandler = new ParserHandlersList(elementCreationHandler, searchCrawler, markdownGenerator);

        Node node = fullParser.parse(markdown);
        MarkdownVisitor visitor = parsePartial(node, path, parserHandler);

        if (visitor.hasPluginWarnings()) {
            reportWarnings(path, visitor.getParameterWarnings());
        }

        if (visitor.isSectionStarted()) {
            parserHandler.onSectionEnd();
        }

        parserHandler.onParsingEnd();

        return new MarkupParserResult(elementCreationHandler.getDocElement(),
                elementCreationHandler.getGlobalAnchorIds(),
                searchCrawler.getSearchEntries(),
                elementCreationHandler.getAuxiliaryFiles(),
                parsePageMeta(node),
                markdownGenerator.getMarkdown());
    }

    @Override
    public PageMeta parsePageMetaOnly(String markdown) {
        Node node = metaOnlyParser.parse(markdown);
        return parsePageMeta(node);
    }

    public void parse(Path path, ParserHandler handler, String markdown) {
        Node node = fullParser.parse(markdown);
        parsePartial(node, path, handler);
    }

    private MarkdownVisitor parsePartial(Node node, Path path, ParserHandler handler) {
        MarkdownVisitor visitor = new MarkdownVisitor(componentsRegistry, path, handler);
        node.accept(visitor);

        return visitor;
    }

    private PageMeta parsePageMeta(Node node) {
        YamlFrontMatterVisitor frontMatterVisitor = new YamlFrontMatterVisitor();
        node.accept(frontMatterVisitor);

        return new PageMeta(frontMatterVisitor.getData());
    }

    private static Parser createCommonMarkParser(PluginParamsFactory pluginParamsFactory) {
        CommonMarkExtension extension = new CommonMarkExtension(pluginParamsFactory);

        return Parser.builder().extensions(Arrays.asList(extension,
                TablesExtension.create(),
                StrikethroughExtension.create(),
                FootnotesExtension.create(),
                YamlFrontMatterExtension.create())).build();
    }

    private void reportWarnings(Path path, Set<PluginParamWarning> parameterWarnings) {
        Log log = componentsRegistry.log();

        log.warn("plugin warnings inside: ", Color.PURPLE, path);
        for (PluginParamWarning parameterWarning : parameterWarnings) {
            log.warn(Color.BLUE, parameterWarning.getPluginId() + " plugin:", FontStyle.NORMAL, " <" + parameterWarning.getParameterName() + "> ",
                    Color.YELLOW, parameterWarning.getMessage());
        }
    }
}
