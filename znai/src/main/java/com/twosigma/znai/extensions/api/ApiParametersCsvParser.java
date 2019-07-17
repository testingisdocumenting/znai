package com.twosigma.znai.extensions.api;

import com.twosigma.znai.extensions.table.CsvParser;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.parser.table.MarkupTableData;
import com.twosigma.znai.parser.table.Row;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ApiParametersCsvParser {
    private final ApiParameters apiParameters;
    private final MarkupParser markupParser;
    private final String csvContent;
    private final Path path;

    public static ApiParameters parse(MarkupParser markupParser, String csvContent) {
        return new ApiParametersCsvParser(markupParser, csvContent).parse();
    }

    private ApiParametersCsvParser(MarkupParser markupParser, String csvContent) {
        this.markupParser = markupParser;
        this.csvContent = csvContent;
        this.apiParameters = new ApiParameters();
        this.path = Paths.get("");
    }

    public ApiParameters parse() {
        MarkupTableData tableData = CsvParser.parseWithHeader(csvContent, "name", "type", "description");
        tableData.forEachRow(this::parseRow);

        return apiParameters;
    }

    private void parseRow(Row row) {
        String name = row.get(0);
        String type = row.get(1);

        MarkupParserResult markupParserResult = markupParser.parse(path, row.get(2));
        List<Map<String, Object>> description = markupParserResult.getDocElement().contentToListOfMaps();

        if (name.contains(".")) {
            addNested(name, type, description);
        } else {
            apiParameters.add(name, type, description);
        }
    }

    private void addNested(String name, String type, List<Map<String, Object>> description) {
        String[] parts = name.split("\\.");
        ApiParameter apiParameter = apiParameters.find(parts[0]);
        for (int i = 1; i < parts.length - 1; i++) {
            apiParameter = apiParameter.find(parts[i]);
        }

        apiParameter.add(parts[parts.length - 1], type, description);
    }
}
