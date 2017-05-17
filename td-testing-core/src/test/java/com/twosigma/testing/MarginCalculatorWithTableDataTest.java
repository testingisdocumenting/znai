package com.twosigma.testing;

import com.twosigma.testing.data.table.TableData;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * @author mykola
 */
public class MarginCalculatorWithTableDataTest {
    MarginCalculator marginCalculator = new MarginCalculator();


    @Test
    public void marginShouldBeZeroIfNoLotsSet() {
        List<Transaction> transactions = createTransaction(null);

        assertEquals(0, marginCalculator.calculate(transactions),
                0.0000001);
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
