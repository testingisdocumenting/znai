/*
 * Copyright 2024 znai maintainers
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

package org.testingisdocumenting.znai.extensions.footnote;

import org.commonmark.ext.footnotes.FootnoteDefinition;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.parser.ParserHandlersList;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownVisitor;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.docelement.DocElementCreationParserHandler;
import org.testingisdocumenting.znai.search.PageSearchEntry;
import org.testingisdocumenting.znai.search.SearchCrawlerParserHandler;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public record ParsedFootnote(FootnoteId id, DocElement docElement, List<PageSearchEntry> searchEntries) {
    public static ParsedFootnote parse(ComponentsRegistry componentsRegistry, Path markdownPath, FootnoteDefinition footnote) {
        var searchHandler = new SearchCrawlerParserHandler();
        DocElementCreationParserHandler docElementsHandler = new DocElementCreationParserHandler(componentsRegistry, markdownPath);
        var parserHandler = new ParserHandlersList(
                docElementsHandler,
                searchHandler
        );

        var visitor = new MarkdownVisitor(componentsRegistry, markdownPath, parserHandler);
        visit(visitor, footnote);

        List<PageSearchEntry> searchEntries = searchHandler.getSearchEntries();
        DocElement docElement = docElementsHandler.getDocElement();

        return new ParsedFootnote(new FootnoteId(footnote.getLabel()), docElement, searchEntries);
    }

    private static void visit(MarkdownVisitor visitor, FootnoteDefinition footnote) {
        var node = footnote.getFirstChild();
        while (node != null) {
            node.accept(visitor);
            node = node.getNext();
        }
    }

    public String allText() {
        return searchEntries.stream().map(PageSearchEntry::extractText).collect(Collectors.joining(" "));
    }
}
