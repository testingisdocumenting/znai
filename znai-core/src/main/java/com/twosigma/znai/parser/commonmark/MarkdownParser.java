/*
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

package com.twosigma.znai.parser.commonmark;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.parser.ParserHandlersList;
import com.twosigma.znai.search.SearchCrawlerParserHandler;
import com.twosigma.znai.structure.PageMeta;
import com.twosigma.znai.parser.docelement.DocElementCreationParserHandler;
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
        CommonMarkExtension extension = new CommonMarkExtension();

        fullParser = Parser.builder().extensions(Arrays.asList(extension,
                TablesExtension.create(),
                StrikethroughExtension.create(),
                YamlFrontMatterExtension.create())).build();

        metaOnlyParser = Parser.builder().extensions(
                Collections.singletonList(YamlFrontMatterExtension.create())).build();
    }

    public MarkupParserResult parse(Path path, String markdown) {
        SearchCrawlerParserHandler searchCrawler = new SearchCrawlerParserHandler();
        DocElementCreationParserHandler elementCreationHandler =
                new DocElementCreationParserHandler(componentsRegistry, path);

        ParserHandlersList parserHandler = new ParserHandlersList(elementCreationHandler, searchCrawler);

        Node node = fullParser.parse(markdown);
        MarkdownVisitor visitor = parsePartial(node, path, parserHandler);

        if (visitor.isSectionStarted()) {
            parserHandler.onSectionEnd();
        }

        parserHandler.onParsingEnd();

        return new MarkupParserResult(elementCreationHandler.getDocElement(),
                elementCreationHandler.getGlobalAnchorIds(),
                searchCrawler.getSearchEntries(),
                elementCreationHandler.getAuxiliaryFiles(),
                parsePageMeta(node));
    }

    @Override
    public PageMeta parsePageMetaOnly(String markdown) {
        Node node = metaOnlyParser.parse(markdown);
        return parsePageMeta(node);
    }

    public void parse(Path path, String markdown, ParserHandler handler) {
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
}
