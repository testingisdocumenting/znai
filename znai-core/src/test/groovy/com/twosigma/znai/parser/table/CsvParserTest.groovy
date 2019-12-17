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

package com.twosigma.znai.parser.table

import org.junit.Test

class CsvParserTest {
    def expectedParsedData = ["columns": [
            ["title": "Account"],
            ["title": "Price"],
            ["title": "Description"]
    ], "data": [
            ["#12BGD3", 100, "custom, table"],
            ["#12BGD3", 150, "chair"],
            ["#91AGB1", 10, "lunch"]
    ]]

    @Test
    void "parse simple csv"() {
        def csvData = CsvParser.parse("""Account, Price, "Description"
#12BGD3, 100, "custom, table"
#12BGD3, 150, chair
#91AGB1, 10, lunch
""")

        csvData.toMap().should == expectedParsedData
    }

    @Test
    void "parse csv without header"() {
        def csvData = CsvParser.parseWithHeader("""
#12BGD3, 100, "custom, table"
#12BGD3, 150, chair
#91AGB1, 10, lunch
""", "Account", "Price", "Description")

        csvData.toMap().should == expectedParsedData
    }

    @Test(expected = RuntimeException)
    void "report columns mismatch"() {
        def csvData = CsvParser.parse("""Account, Price, "Description"
#12BGD3, 100, "custom, table"
#12BGD3, 150, chair, extra
#91AGB1, 10, lunch
""")
    }
}
