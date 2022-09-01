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

package org.testingisdocumenting.znai.extensions.table

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParams
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils

class TableFencePluginTest {
    @Test
    void "handle params"() {
        def props = process([title: "my table", Price: [width: 100, align: "right"]], """
Account, Price, Description
#12BGD3, 100, custom table with a long attachment
#12BGD3, 150, chair
#91AGB1, 10, lunch
""")

        props.should == [table: [columns: [[title: "Account"], [title: "Price", width: 100, align: "right"],
                                           [title: "Description"]],
                                 data: [[[[markup: "#12BGD3", type: "TestMarkup"]],
                                         [[markup: 100, type: "TestMarkup"]],
                                         [[markup: "custom table with a long attachment", type: "TestMarkup"]]],
                                        [[[markup: "#12BGD3", type: "TestMarkup"]],
                                         [[markup: 150, type: "TestMarkup"]],
                                         [[markup: "chair", type: "TestMarkup"]]],
                                        [[[markup: "#91AGB1", type: "TestMarkup"]],
                                         [[markup: 10, type: "TestMarkup"]],
                                         [[markup: "lunch", type: "TestMarkup"]]]]], title: "my table"]

    }

    private static Map<String, ?> process(Map<String, ?> params, String content) {
        return PluginsTestUtils.processFenceAndGetProps(new PluginParams("table", params), content)
    }
}
