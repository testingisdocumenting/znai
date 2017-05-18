package com.twosigma.testing

import com.twosigma.testing.data.table.TableData
import org.junit.Test

/**
 * @author mykola
 */
class MarginCalculatorWithGroovyTableDataTest {
    private MarginCalculator marginCalculator = new MarginCalculator()

    @Test
    void "margin should be zero if no lots set"() {
        def transactionsData = ["symbol" | "lot" | "price"] {
                                 ___________________________
                                 "SYM.B" |     0 |     8
                                 "SYM.C" |     0 |    19 }

        def margin = marginCalculator.calculate(createTransaction(transactionsData))
        margin.should == 0
    }

    private static List<Transaction> createTransaction(TableData tableData) {
        tableData.collect {
            new Transaction(symbol: it.symbol, lot: it.lot, price: it.price)
        }
    }
}
