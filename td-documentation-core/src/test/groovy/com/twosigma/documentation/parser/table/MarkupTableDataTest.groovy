package com.twosigma.documentation.parser.table

import org.junit.Before
import org.junit.Test

import static com.twosigma.testing.Ddjt.actual
import static com.twosigma.testing.Ddjt.equal
import static java.util.stream.Collectors.toList

/**
 * @author mykola
 */
class MarkupTableDataTest {
    MarkupTableData table

    @Before
    void init() {
        table = new MarkupTableData()
        table.addColumn('a')
        table.addColumn('b')
        table.addColumn('C')

        table.addRow(new Row(['a1', 'b1', 'c1']))
        table.addRow(new Row(['a2', 'b2', 'c2']))
    }

    @Test
    void "should rearange and filter out columns by case insensitive names"() {
        def newTable = table.withColumnsInOrder(['c', 'A'])
        // TODO fix shortcut 'should ==' for maps
        actual(newTable.toMap()).should(equal([columns:[[title: 'C'], [title: 'a']], data:[['c1', 'a1'], ['c2', 'a2']]]))
    }

    @Test
    void "should have a stream of column names"() {
        table.columnNamesStream().should == ['a', 'b', 'C']
    }

    @Test
    void "should have a stream of all values"() {
        table.allValuesStream().should == ['a1', 'b1', 'c1', 'a2', 'b2', 'c2']
    }

    @Test
    void "should have a text representation of a table content"() {
        table.allText().should == 'a b C a1 b1 c1 a2 b2 c2'
    }
}
