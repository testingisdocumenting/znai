package com.twosigma.documentation.extensions.table;

import com.twosigma.documentation.parser.table.MarkupTableData;
import com.twosigma.documentation.parser.table.Row;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

/**
 * @author mykola
 */
class CsvParser {
    private CsvParser() {
    }

    public static MarkupTableData parse(String content) {
        try {
            MarkupTableData tableData = new MarkupTableData();

            CSVParser csvRecords = CSVFormat.RFC4180.withFirstRecordAsHeader().
                    withIgnoreSurroundingSpaces().
                    withIgnoreEmptyLines().
                    withTrim().
                    withDelimiter(',').
                    parse(new StringReader(content));

            Map<String, Integer> headerMap = csvRecords.getHeaderMap();
            headerMap.keySet().forEach(tableData::addColumn);

            for (CSVRecord record : csvRecords) {
                Row row = new Row();
                record.forEach(row::add);

                if (record.size() != headerMap.size()) {
                    throw new RuntimeException("record mismatches header. header: " + headerMap.keySet() +
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
