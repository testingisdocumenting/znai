package com.twosigma.documentation.search

import org.junit.Test

/**
 * @author mykola
 */
class SiteSearchEntriesTest {
    @Test
    void "should generate XML document"() {
        def entries = new SiteSearchEntries()
        entries.addAll([
                new SiteSearchEntry('/doc-id/title1','full title 1', new SearchText('text 1', SearchScore.HIGH)),
                new SiteSearchEntry('/doc-id/title2','full title 2', new SearchText('text 2', SearchScore.STANDARD))])

        entries.toXml().should == '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>\n' +
                '<mdoc>\n' +
                '    <entry>\n' +
                '        <fullTitle>full title 1</fullTitle>\n' +
                '        <text>\n' +
                '            <score>HIGH</score>\n' +
                '            <text>text 1</text>\n' +
                '        </text>\n' +
                '        <url>/doc-id/title1</url>\n' +
                '    </entry>\n' +
                '    <entry>\n' +
                '        <fullTitle>full title 2</fullTitle>\n' +
                '        <text>\n' +
                '            <score>STANDARD</score>\n' +
                '            <text>text 2</text>\n' +
                '        </text>\n' +
                '        <url>/doc-id/title2</url>\n' +
                '    </entry>\n' +
                '</mdoc>\n'
    }
}
