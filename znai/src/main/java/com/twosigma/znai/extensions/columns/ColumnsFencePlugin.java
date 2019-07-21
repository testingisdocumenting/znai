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

package com.twosigma.znai.extensions.columns;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.ColonDelimitedKeyValues;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.parser.docelement.DocElement;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;

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
        return Stream.of(columnsDefinitions.get("left"), columnsDefinitions.get("right")).
                map(this::buildColumn).collect(toList());
    }

    private Map<String, Object> buildColumn(String markup) {
        MarkupParserResult parserResult = parser.parse(markupPath, markup);
        columnsParserResult.add(parserResult);

        Map<String, Object> column = new LinkedHashMap<>();
        column.put("content", parserResult.getDocElement().contentToListOfMaps());

        return column;
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return columnsParserResult.stream().flatMap(pr -> pr.getAuxiliaryFiles().stream());
    }

    @Override
    public SearchText textForSearch() {
        String textFromAllColumns = columnsParserResult.stream().map(MarkupParserResult::getAllText)
                .collect(joining(" "));

        return SearchScore.STANDARD.text(textFromAllColumns);
    }
}