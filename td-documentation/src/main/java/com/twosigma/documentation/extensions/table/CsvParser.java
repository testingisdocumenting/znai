package com.twosigma.documentation.extensions.table;

import com.twosigma.documentation.parser.table.MarkupTableData;
import com.twosigma.documentation.parser.table.Row;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author mykola
 */
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
                record.forEach(v -> row.add(convert(v)));

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

    private static Object convert(Object v) {
        String s = v.toString();
        Scanner scanner = new Scanner(s);
        return scanner.hasNextBigDecimal() ? new BigDecimal(s) : s;
    }
}
