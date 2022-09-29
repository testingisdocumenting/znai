/*
 * Copyright 2020 znai maintainers
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

import org.junit.Test

class RowTest {
    @Test
    void "should filter out row based on provided indexes"() {
        def row = new Row()
        ['a', 'b', 'c', 'd'].each { row.add(it) }

        def filtered = row.onlyWithIdxs([1, 2])
        filtered.getData().should == ['b', 'c']
    }

    @Test
    void "should map values based on mapping"() {
        def row = new Row()
        row.add('A')
        row.add('B')
        row.add('C')
        row.add('D')

        def newRow = row.map(new MapBasedMarkupTableMapping([B: 'hello', D: 'world']))

        row.data.should == ['A', 'B', 'C', 'D']
        newRow.data.should == ['A', 'hello', 'C', 'world']
    }

    @Test
    void "should check if row contains provided regexp"() {
        def noText = new Row()
        noText.add(8)
        noText.add(10)
        noText.matchRegexp(~/hello/).should == false
        noText.matchRegexp(~/1/).should == true
        noText.matchRegexp(~/\d/).should == true

        def withText = new Row()
        withText.add("testing")
        withText.add(20)
        withText.add("word")
        withText.matchRegexp(~/help/).should == false
        withText.matchRegexp(~/te.*ing/).should == true
        withText.matchRegexp(~/w..d/).should == true
    }
}
