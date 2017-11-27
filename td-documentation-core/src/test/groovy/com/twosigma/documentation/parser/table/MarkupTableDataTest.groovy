package com.twosigma.documentation.parser.table

import org.junit.Test

import static com.twosigma.testing.Ddjt.actual
import static com.twosigma.testing.Ddjt.equal

/**
 * @author mykola
 */
class MarkupTableDataTest {
    @Test
    void "should rearange and filters out columns by case insensitive names"() {
        def table = new MarkupTableData()
        table.addColumn('a')
        table.addColumn('b')
        table.addColumn('C')

        table.addRow(new Row(['a1', 'b1', 'c1']))
        table.addRow(new Row(['a2', 'b2', 'c2']))

        def newTable = table.withColumnsInOrder(['c', 'A'])
        // TODO fix shortcut 'should ==' for maps
        actual(newTable.toMap()).should(equal([columns:[[title: 'C'], [title: 'a']], data:[['c1', 'a1'], ['c2', 'a2']]]))
    }
}
