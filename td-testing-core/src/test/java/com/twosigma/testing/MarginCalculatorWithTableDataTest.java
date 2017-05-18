package com.twosigma.testing;

import com.twosigma.testing.data.table.TableData;
import org.junit.Test;

import java.util.List;

import static com.twosigma.testing.data.table.TableData.header;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * @author mykola
 */
public class MarginCalculatorWithTableDataTest {
    private MarginCalculator marginCalculator = new MarginCalculator();

    @Test
    public void marginShouldBeZeroIfNoLotsSet() {
        TableData transactionsData = header("symbol", "lot", "price").values(
                                             "SYM.B",  0.0,    8.0,
                                             "SYM.C",  0.0,    19.0);

        double margin = marginCalculator.calculate(createTransaction(transactionsData));
        assertEquals(0, margin, 0.0000001);
    }

    private static List<Transaction> createTransaction(TableData tableData) {
        return tableData.rowsStream().map(r -> {
            Transaction t = new Transaction();
            t.setSymbol(r.get("symbol"));
            t.setLot(r.get("lot"));
            t.setPrice(r.get("price"));

            return t;
        }).collect(toList());
    }
}
