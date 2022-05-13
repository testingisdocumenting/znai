/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.charts

import org.junit.Test
import org.testingisdocumenting.znai.utils.ResourceUtils

class ChartDataCsvParserTest {
    @Test
    void "chart csv data parser"() {
        def data = ChartDataCsvParser.parse(ResourceUtils.textContent("multi-bar.csv"))
        data.toMap().should == [
                labels: ["x", "price", "tax", "service fee"],
                data: [
                        ["one", 10, 5, 3],
                        ["two", 30, 6, 1],
                        ["xyz", 40, 2, 4]
                ]
        ]
    }
}
