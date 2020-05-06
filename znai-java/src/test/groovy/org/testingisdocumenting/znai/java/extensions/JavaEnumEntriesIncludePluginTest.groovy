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

package org.testingisdocumenting.znai.java.extensions

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.testingisdocumenting.znai.parser.docelement.DocElement
import org.junit.Test

class JavaEnumEntriesIncludePluginTest {
    static final def entryOneDescription = [[type: 'Paragraph', content: [[text: 'description of ', type: 'SimpleText'],
                                                                          [type: 'StrongEmphasis', content: [[text: 'entry one', type: 'SimpleText']]]]]]


    static final def entryTwoDescription = [[type: 'Paragraph', content: [[text: 'description of entry two', type: 'SimpleText']]]]

    @Test
    void "should generate table component with enum entries"() {
        def result = process('Enum.java', '')

        result.should == [
                [
                        type      : 'ApiParameters',
                        parameters: [
                                [name: 'ENTRY_ONE_WITH_A_LONG_NAME', type: '', description: entryOneDescription],
                                [name: 'ENTRY_TWO', type: '', description: entryTwoDescription]],
                ]]
    }

    @Test
    void "should exclude deprecated enums when specified"() {
        def result = process('Enum.java', '{"excludeDeprecated": true}')

        result.should == [
                [
                        type             : 'ApiParameters',
                        excludeDeprecated: true,
                        parameters       : [
                                [name: 'ENTRY_ONE_WITH_A_LONG_NAME', type: '', description: entryOneDescription]],
                ]]
    }

    private static List<Map<String, Object>> process(String fileName, String params) {
        return PluginsTestUtils.process(":include-java-enum-entries: $fileName $params")*.toMap()
    }
}
