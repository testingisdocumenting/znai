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

package org.testingisdocumenting.znai.reference;

import org.testingisdocumenting.znai.parser.table.CsvTableParser;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.util.Map;

public class DocReferencesParser {
    private DocReferencesParser() {
    }

    public static DocReferences parse(String content) {
        content = content.trim();
        return content.startsWith("{") ?
                parseJson(content) :
                parseCsv(content);
    }

    public static DocReferences parseJson(String content) {
        Map<String, ?> jsonReferences = JsonUtils.deserializeAsMap(content);
        DocReferences result = new DocReferences();
        jsonReferences.forEach((k, v) -> result.add(k, v.toString()));

        return result;
    }

    public static DocReferences parseCsv(String content) {
        MarkupTableData tableData = CsvTableParser.parseWithHeader(content, "reference", "url");

        DocReferences result = new DocReferences();
        tableData.forEachRow(row -> result.add(row.get(0), row.get(1)));

        return result;
    }
}