package com.twosigma.testing.data.table.comparison

import org.junit.Test

/**
 * @author mykola
 */
class TableDataComparisonTest {
    @Test
    void "should report missing columns"() {
        def actual = ["a" | "b" | "c"] {
                      ______________
                       10 | 20  | 30 }

        def expected = ["a" | "b" | "e" | "c" | "d"] {
                        ____________________________
                         10 | 20  | 1   | 30  | 40 }

        def result = TableDataComparison.compare(actual, expected)
        result.missingColumns.should == ["d", "e"]
    }

    @Test
    void "should report missing rows (no key columns)"() {
        def actual = ["a" | "b" | "c"] {
                      ______________
                       10 | 20  | 30 }

        def expected = ["a" | "b" | "c"] {
                        ______________
                        10 | 20 | 30
                        20 | 60 | 130
                        40 | 80 | 230 }

        def result = TableDataComparison.compare(actual, expected)
        def missingRows = result.getMissingRows()

        missingRows.size().should == 2
        missingRows.get(0).should == [a: 20, b: 60, c: 130]
        missingRows.get(1).should == [a: 40, b: 80, c: 230]
    }

    @Test
    void "should report extra rows when (no key columns)"() {
        def actual = ["a" | "b" | "c"] {
                       ______________
                       10 | 20  | 30
                       22 | 62  | 132
                       42 | 82  | 232 }

        def expected = ["a" | "b" | "c"] {
                        ______________
                        10 | 20 | 30 }

        def result = TableDataComparison.compare(actual, expected)
        def extraRows = result.getExtraRows()

        extraRows.size().should == 2
        extraRows.get(0).should == [a: 22, b: 62, c: 132]
        extraRows.get(1).should == [a: 42, b: 82, c: 232]
    }
}
