package com.twosigma.znai.search

import org.junit.Test

class GlobalSearchEntriesTest {
    @Test
    void "should generate XML document"() {
        def entries = new GlobalSearchEntries()
        entries.addAll([
                new GlobalSearchEntry('/doc-id/title1','full title 1', new SearchText('text 1', SearchScore.HIGH)),
                new GlobalSearchEntry('/doc-id/title2','full title 2', new SearchText('text 2', SearchScore.STANDARD))])

        println entries.toXml()
        entries.toXml().should == '<mdoc>\n' +
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
                '</mdoc>\n'
    }
}
