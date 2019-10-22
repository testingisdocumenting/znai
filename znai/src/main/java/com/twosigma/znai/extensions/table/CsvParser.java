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

package com.twosigma.znai.extensions.table;

import com.twosigma.znai.parser.table.MarkupTableData;
import com.twosigma.znai.parser.table.Row;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CsvParser {
    private CsvParser() {
    }

    public static MarkupTableData parse(String content) {
        return parse(content, Collections.emptyList());
    }

    public static MarkupTableData parseWithHeader(String content, String... header) {
        return parse(content, Arrays.asList(header));
    }

    private static MarkupTableData parse(String content, List<String> header) {
        try {
            MarkupTableData tableData = new MarkupTableData();

            CSVFormat csvFormat = CSVFormat.RFC4180;
            if (header.isEmpty()) {
                csvFormat = csvFormat.withFirstRecordAsHeader();
            }

            CSVParser csvRecords = csvFormat.
                    withIgnoreSurroundingSpaces().
                    withIgnoreEmptyLines().
                    withTrim().
                    withDelimiter(',').
                    parse(new StringReader(content));

            Collection<String> headerToUse = header.isEmpty() ?
                    csvRecords.getHeaderMap().keySet() :
                    header;

            headerToUse.forEach(tableData::addColumn);

            for (CSVRecord record : csvRecords) {
                Row row = new Row();
                record.forEach(row::add);

                if (record.size() != headerToUse.size()) {
                    throw new RuntimeException("record mismatches header. header: " + headerToUse +
                            "; record: " + row.getData());
                }

                tableData.addRow(row);
            }

            return tableData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
