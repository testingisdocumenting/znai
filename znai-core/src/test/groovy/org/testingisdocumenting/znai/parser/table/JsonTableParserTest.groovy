/*
 * Copyright 2020 znai maintainers
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

class JsonTableParserTest {
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
    void "parse simple json"() {
        def markupTableData = JsonTableParser.parse("""[
  {
    "Account": "#12BGD3",
    "Price": 100,
    "Description": "custom, table"
  },
  {
    "Account": "#12BGD3",
    "Price": 150,
    "Description": "chair"
  },
  {
    "Account": "#91AGB1",
    "Price": 10,
    "Description": "lunch"
  }
]
""")

        markupTableData.toMap().should == expectedParsedData
    }

}
