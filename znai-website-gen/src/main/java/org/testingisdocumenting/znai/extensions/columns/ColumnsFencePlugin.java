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

package org.testingisdocumenting.znai.extensions.columns;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.ColonDelimitedKeyValues;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ColumnsFencePlugin implements FencePlugin {
    private List<MarkupParserResult> columnsParserResult;
    private Path markupPath;
    private MarkupParser parser;

    @Override
    public String id() {
        return "columns";
    }

    @Override
    public FencePlugin create() {
        return new ColumnsFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        this.markupPath = markupPath;
        this.columnsParserResult = new ArrayList<>();

        parser = componentsRegistry.defaultParser();

        ColonDelimitedKeyValues columnsDefinitions = new ColonDelimitedKeyValues(content);
        DocElement docElement = new DocElement("Columns",
                "columns", buildColumns(columnsDefinitions),
                "config", pluginParams.getOpts().toMap());

        return PluginResult.docElement(docElement);
    }

    private List<Map<String, Object>> buildColumns(ColonDelimitedKeyValues columnsDefinitions) {
        List<String> definitions = new ArrayList<>();
        definitions.add(columnsDefinitions.get("left"));
        if (columnsDefinitions.has("middle")) {
            definitions.add(columnsDefinitions.get("middle"));
        }
        definitions.add(columnsDefinitions.get("right"));

        return definitions.stream()
                .map(this::buildColumn).collect(toList());
    }

    private Map<String, Object> buildColumn(String markup) {
        MarkupParserResult parserResult = parser.parse(markupPath, markup);
        columnsParserResult.add(parserResult);

        Map<String, Object> column = new LinkedHashMap<>();
        column.put("content", parserResult.docElement().contentToListOfMaps());

        return column;
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return columnsParserResult.stream().flatMap(pr -> pr.auxiliaryFiles().stream());
    }

    @Override
    public List<SearchText> textForSearch() {
        String textFromAllColumns = columnsParserResult.stream().map(MarkupParserResult::getAllText)
                .collect(joining(" "));

        return List.of(SearchScore.STANDARD.text(textFromAllColumns));
    }
}