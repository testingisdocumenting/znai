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

package org.testingisdocumenting.znai.parser.sphinx;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.ParserHandlersList;
import org.testingisdocumenting.znai.parser.docelement.DocElementCreationParserHandler;
import org.testingisdocumenting.znai.search.SearchCrawlerParserHandler;
import org.testingisdocumenting.znai.structure.PageMeta;

import java.nio.file.Path;
import java.util.Collections;

public class SphinxDocTreeParser implements MarkupParser {
    private ComponentsRegistry componentsRegistry;

    public SphinxDocTreeParser(ComponentsRegistry componentsRegistry) {
        this.componentsRegistry = componentsRegistry;
    }

    @Override
    public MarkupParserResult parse(Path path, String docXml) {
        SearchCrawlerParserHandler searchCrawler = new SearchCrawlerParserHandler();
        DocElementCreationParserHandler elementCreationHandler = new DocElementCreationParserHandler(componentsRegistry, path);

        ParserHandlersList parserHandler = new ParserHandlersList(elementCreationHandler, searchCrawler);

        DocTreeDomXmlParser xmlParser = new DocTreeDomXmlParser(componentsRegistry, path, parserHandler);
        xmlParser.parse(docXml);

        return new MarkupParserResult(elementCreationHandler.getDocElement(),
                Collections.emptyList(),
                searchCrawler.getSearchEntries(),
                elementCreationHandler.getAuxiliaryFiles(),
                new PageMeta());
    }

    @Override
    public PageMeta parsePageMetaOnly(String markup) {
        return new PageMeta();
    }
}
