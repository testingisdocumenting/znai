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

package org.testingisdocumenting.znai.search

import org.junit.Test

class GlobalSearchEntriesTest {
    @Test
    void "should generate XML document"() {
        def entries = new GlobalSearchEntries()
        entries.addAll([
                new GlobalSearchEntry('/doc-id/title1','full title 1', new SearchText('text 1', SearchScore.HIGH)),
                new GlobalSearchEntry('/doc-id/title2','full title 2', new SearchText('text 2', SearchScore.STANDARD))])

        println entries.toXml()
        entries.toXml().should == '<znai>\n' +
                '  <entry>\n' +
                '    <url>/doc-id/title1</url>\n' +
                '    <fullTitle>full title 1</fullTitle>\n' +
                '    <text>\n' +
                '      <text>text 1</text>\n' +
                '      <score>HIGH</score>\n' +
                '    </text>\n' +
                '  </entry>\n' +
                '  <entry>\n' +
                '    <url>/doc-id/title2</url>\n' +
                '    <fullTitle>full title 2</fullTitle>\n' +
                '    <text>\n' +
                '      <text>text 2</text>\n' +
                '      <score>STANDARD</score>\n' +
                '    </text>\n' +
                '  </entry>\n' +
                '</znai>\n'
    }
}
