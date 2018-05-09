package com.twosigma.documentation.extensions.columns;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.ColonDelimitedKeyValues;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
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