package com.twosigma.documentation.extensions.table;

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

    public static CsvData parse(String content) {
        try {
            CsvData csvData = new CsvData();

            CSVParser csvRecords = CSVFormat.EXCEL.withHeader().parse(new StringReader(content));
            Map<String, Integer> headerMap = csvRecords.getHeaderMap();
            System.out.println(headerMap);

            headerMap.keySet().forEach(csvData::addColumn);

            for (CSVRecord record : csvRecords) {
                Row row = new Row();
                record.forEach(row::add);

                csvData.addRow(row);
            }

            return csvData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
