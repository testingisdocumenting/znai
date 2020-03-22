/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.parser.table

import org.junit.Before
import org.junit.Test

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
        newTable.toMap().should == [columns:[[title: 'C'], [title: 'a']], data:[['c1', 'a1'], ['c2', 'a2']]]
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

    @Test
    void "should handle null content for text representation"() {
        def tableWithNull = new MarkupTableData()
        tableWithNull.addColumn('a')
        tableWithNull.addColumn('b')

        tableWithNull.addRow(new Row(['a1', null]))

        tableWithNull.allText().should == 'a b a1'
    }
}
