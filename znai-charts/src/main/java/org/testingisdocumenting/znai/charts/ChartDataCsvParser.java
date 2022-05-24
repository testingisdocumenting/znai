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

package org.testingisdocumenting.znai.charts;

import org.testingisdocumenting.znai.parser.table.CsvTableParser;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;

import java.util.List;

class ChartDataCsvParser {
    private ChartDataCsvParser() {
    }

    public static ChartData parse(String content) {
        MarkupTableData tableData = CsvTableParser.parse(content);
        List<List<Object>> data = tableData.getData();

        if (data.isEmpty()) {
            throw new IllegalArgumentException("no data is present");
        }

        if (data.get(0).size() <= 1) {
            throw new IllegalArgumentException("chart requires at least two columns of data");
        }

        return new ChartData(tableData.getColumnTitles(), tableData.getDataConvertingNumbers());
    }
}
