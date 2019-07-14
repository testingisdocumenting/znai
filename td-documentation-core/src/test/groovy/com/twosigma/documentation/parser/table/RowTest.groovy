package com.twosigma.documentation.parser.table

import org.junit.Test

class RowTest {
    @Test
    void "should filter out row based on provided indexes"() {
        def row = new Row()
        ['a', 'b', 'c', 'd'].each { row.add(it) }

        def filtered = row.onlyWithIdxs([1, 2])
        filtered.getData().should == ['b', 'c']
    }
}
