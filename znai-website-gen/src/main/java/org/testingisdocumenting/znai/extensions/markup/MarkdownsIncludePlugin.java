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

package org.testingisdocumenting.znai.extensions.markup;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.*;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class MarkdownsIncludePlugin implements IncludePlugin {
    private static final String SORT_KEY = "sort";
    private static final String ASCENDING = "ascending";
    private static final String DESCENDING = "descending";

    private List<Path> markdowns;
    private List<MarkupParserResult> parserResults;
    private PluginParamsOpts opts;

    @Override
    public String id() {
        return "markdowns";
    }

    @Override
    public IncludePlugin create() {
        return new MarkdownsIncludePlugin();
    }

    @Override
    public PluginParamsDefinition parameters() {
        return new PluginParamsDefinition()
                .add(SORT_KEY, PluginParamType.STRING, "sort direction, ascending or descending (descending is default)", "\"ascending\"");
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        opts = pluginParams.getOpts();

        Path dir = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        MarkupParser parser = componentsRegistry.defaultParser();

        markdowns = markdowns(dir).collect(Collectors.toList());
        parserResults = markdowns.stream()
                .map(p -> parser.parse(markupPath, FileUtils.fileTextContent(p)))
                .collect(Collectors.toList());
        Stream<DocElement> elements = parserResults.stream()
                .flatMap(r -> r.docElement().getContent().stream());

        return PluginResult.docElements(elements);
    }

    private Stream<Path> markdowns(Path dir) {
        try {
            return Files.list(dir).filter(f -> f.toString().endsWith(".md")).sorted(createComparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Comparator<Path> createComparator() {
        Comparator<Path> comparator = Comparator.comparing(Path::toString);

        String sortDirection = opts.get(SORT_KEY, "");
        if (sortDirection.isEmpty() || sortDirection.equals(DESCENDING)) {
            return comparator.reversed();
        }

        if (!sortDirection.equals(ASCENDING)) {
            // TODO formal parameters enums support
            throw new IllegalArgumentException(SORT_KEY + " only accepts <" + ASCENDING + ">, or <" + DESCENDING + ">");
        }

        return comparator;
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return markdowns.stream().map(AuxiliaryFile::builtTime);
    }

    @Override
    public SearchText textForSearch() {
        String textFromMarkupResults = parserResults.stream()
                .map(MarkupParserResult::getAllText)
                .collect(joining(" "));

        return SearchScore.STANDARD.text(textFromMarkupResults);
    }
}
