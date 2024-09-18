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

package org.testingisdocumenting.znai.search

import org.junit.Test

class GlobalSearchEntriesTest {
    @Test
    void "should generate XML document"() {
        def entries = new GlobalSearchEntries()
        entries.addAll([
                new GlobalSearchEntry('/doc-id/title1', 'full title 1', 'text 1'),
                new GlobalSearchEntry('/doc-id/title2', 'full title 2', 'text 2')])

        println entries.toXml()
        entries.toXml().should == '<znai>\n' +
                '  <entry>\n' +
                '    <url>/doc-id/title1</url>\n' +
                '    <fullTitle>full title 1</fullTitle>\n' +
                '    <text>\n' +
                '      <score>STANDARD</score>\n' +
                '      <text>text 1</text>\n' +
                '    </text>\n' +
                '  </entry>\n' +
                '  <entry>\n' +
                '    <url>/doc-id/title2</url>\n' +
                '    <fullTitle>full title 2</fullTitle>\n' +
                '    <text>\n' +
                '      <score>STANDARD</score>\n' +
                '      <text>text 2</text>\n' +
                '    </text>\n' +
                '  </entry>\n' +
                '</znai>\n'
    }

    @Test
    void "should handle ansi sequences"() {
        def entries = new GlobalSearchEntries()
        entries.addAll([
                new GlobalSearchEntry('/doc-id/title', 'title',
                        "\u001B[1mwebtau:\u001B[m000\u001B" +
                                "[1m>\u001B[m http.get(\"https://jsonplaceholder.typicode.com/todos/1\")" +
                                " \u001B[33m> (\u001B[32m342ms\u001B[33m)\u001B[0m")])

        entries.toXml().should == '<znai>\n' +
                '  <entry>\n' +
                '    <url>/doc-id/title</url>\n' +
                '    <fullTitle>title</fullTitle>\n' +
                '    <text>\n' +
                '      <score>STANDARD</score>\n' +
                '      <text>webtau:000> http.get("https://jsonplaceholder.typicode.com/todos/1") > (342ms)</text>\n' +
                '    </text>\n' +
                '  </entry>\n' +
                '</znai>\n'
    }
}
