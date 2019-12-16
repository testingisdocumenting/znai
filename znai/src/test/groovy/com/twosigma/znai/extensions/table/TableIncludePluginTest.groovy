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

package com.twosigma.znai.extensions.table

import com.twosigma.znai.extensions.include.PluginsTestUtils
import org.junit.Test

class TableIncludePluginTest {
    @Test
    void "should read table from csv by detecting format"() {
        def element = process('test-table.csv')

        element.shoul == [type: 'Table', table: [columns: [[title: 'Account'], [title: 'Price'], [title: 'Description']],
                                                 data   : [[[[type: 'TestMarkup', markup: '#12BGD3']], [[type: 'TestMarkup', markup: '100']],
                                                            [[type: 'TestMarkup', markup: 'custom table with a long attachment']]],
                                                           [[[type: 'TestMarkup', markup: '#12BGD3']], [[type: 'TestMarkup', markup: '150']],
                                                            [[type: 'TestMarkup', markup: 'chair']]]]]]
    }

    @Test
    void "should read table from json by detecting format"() {
        def element = process('test-table.json')

        element.should == [type: 'Table', table: [columns: [[title: 'Account'], [title: 'Price'], [title: 'Description']],
                                                  data   : [[[[type: 'TestMarkup', markup: '#12BGD3']], [[type: 'TestMarkup', markup: '100']],
                                                             [[type: 'TestMarkup', markup: 'custom table with a long attachment']]],
                                                            [[[type: 'TestMarkup', markup: '#12BGD3']], [[type: 'TestMarkup', markup: '150']],
                                                             [[type: 'TestMarkup', markup: 'chair']]]]]]
    }

    @Test
    void "should filter out columns from json based on case insensitive provided names"() {
        def element = process('test-table.json', '{columns: ["account", "description"]}')

        element.should == [type: 'Table', table: [columns: [[title: 'Account'], [title: 'Description']],
                                                  data   : [[[[type: 'TestMarkup', markup: '#12BGD3']],
                                                             [[type: 'TestMarkup', markup: 'custom table with a long attachment']]],
                                                            [[[type: 'TestMarkup', markup: '#12BGD3']],
                                                             [[type: 'TestMarkup', markup: 'chair']]]]]]
    }

    @Test
    void "should filter out columns from csv based on case insensitive provided names"() {
        def element = process('test-table.csv', '{columns: ["account", "description"]}')

        element.should == [type: 'Table', table: [columns: [[title: 'Account'], [title: 'Description']],
                                                  data   : [[[[type: 'TestMarkup', markup: '#12BGD3']],
                                                             [[type: 'TestMarkup', markup: 'custom table with a long attachment']]],
                                                            [[[type: 'TestMarkup', markup: '#12BGD3']],
                                                             [[type: 'TestMarkup', markup: 'chair']]]]]]
    }

    private static def process(String fileName, String meta = '') {
        def result = PluginsTestUtils.process(":include-table: $fileName $meta")
        return result[0].toMap()
    }
}
