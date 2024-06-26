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

package org.testingisdocumenting.znai.extensions.api;

import org.testingisdocumenting.znai.parser.table.CsvTableParser;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.parser.table.Row;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ApiParametersCsvParser {
    private final ApiParameters apiParameters;
    private final MarkupParser markupParser;
    private final String csvContent;
    private final Path path;

    public static ApiParameters parse(String anchorPrefix, MarkupParser markupParser, String csvContent) {
        return new ApiParametersCsvParser(anchorPrefix, markupParser, csvContent).parse();
    }

    private ApiParametersCsvParser(String anchorPrefix, MarkupParser markupParser, String csvContent) {
        this.markupParser = markupParser;
        this.csvContent = csvContent;
        this.apiParameters = new ApiParameters(anchorPrefix);
        this.path = Paths.get("");
    }

    public ApiParameters parse() {
        MarkupTableData tableData = CsvTableParser.parseWithHeader(csvContent, "name", "type", "description");
        tableData.forEachRow(this::parseRow);

        return apiParameters;
    }

    private void parseRow(Row row) {
        String name = row.get(0);
        ApiLinkedText type = new ApiLinkedText(row.get(1));

        MarkupParserResult markupParserResult = markupParser.parse(path, row.get(2));
        List<Map<String, Object>> description = markupParserResult.docElement().contentToListOfMaps();

        boolean escapedName = name.startsWith("'") && name.endsWith("'");
        if (name.contains(".") && !name.contains("..") && !escapedName) {
            addNested(name, type, description, markupParserResult.getAllText());
        } else {
            name = escapedName ? name.substring(1, name.length()-1) : name;
            apiParameters.add(name, type, description, markupParserResult.getAllText());
        }
    }

    private void addNested(String name, ApiLinkedText type, List<Map<String, Object>> description, String textForSearch) {
        String[] parts = name.split("\\.");
        ApiParameter apiParameter = apiParameters.find(parts[0]);
        for (int i = 1; i < parts.length - 1; i++) {
            apiParameter = apiParameter.find(parts[i]);
        }

        apiParameter.add(parts[parts.length - 1], type, description, textForSearch);
    }
}
