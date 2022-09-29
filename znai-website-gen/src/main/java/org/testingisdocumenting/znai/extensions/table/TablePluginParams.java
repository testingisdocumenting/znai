/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.extensions.table;

import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinitionCommon;

class TablePluginParams {
    static final String INCLUDE_ROWS_REGEXP_KEY = "includeRowsRegexp";
    static final String EXCLUDE_ROWS_REGEXP_KEY = "excludeRowsRegexp";

    private static final PluginParamsDefinition commonParamsDefinition = new PluginParamsDefinition()
            .add(PluginParamsDefinitionCommon.container)
            .add("mappingPath", PluginParamType.STRING, "csv file path with values mapping", "\"mapping.csv\"")
            .add("columns", PluginParamType.LIST_OR_SINGLE_STRING, "list of columns to include/re-arrange", "[\"colA\", \"colB\"]")
            .add("highlightRow", PluginParamType.LIST_OR_SINGLE_NUMBER, "row indexes to highlight", "[2, 5]")
            .add("minColumnWidth", PluginParamType.NUMBER, "minimum columns width", "300")
            .add("wide", PluginParamType.BOOLEAN, "use wide mode for the table", "true")
            .add(INCLUDE_ROWS_REGEXP_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                    "include rows that match provided list or a single value of regular expressions",
                    "[\"hello\", \"\\d+\"]")
            .add(EXCLUDE_ROWS_REGEXP_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                    "exclude rows that match provided list or a single value of regular expressions",
                    "[\"hello\", \"\\d+\"]");

    static PluginParamsDefinition paramsFromColumnNames(MarkupTableDataFromContentAndParams tableData) {
        PluginParamsDefinition result = new PluginParamsDefinition();
        result.add(commonParamsDefinition);

        tableData.columnNamesStream().forEach(columnName ->
                result.add(columnName, PluginParamType.OBJECT, "column <" + columnName + "> config", "{width: \"50%\"}"));

        return result;
    }
}
