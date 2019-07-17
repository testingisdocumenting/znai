package com.twosigma.znai.structure

import org.junit.Test

class PlainTextTocGeneratorTest {
    @Test
    void "should create top level TOC from nested text structure"() {
        def toc = new PlainTextTocGenerator().generate("""
chapter1
    page-a
    page-b
chapter2
    page-c""")

        toc.toListOfMaps().should == [[sectionTitle: 'Chapter1', dirName: 'chapter1',
                                       items       : [[sectionTitle       : 'Chapter1', pageTitle: 'Page A', fileName: 'page-a', dirName: 'chapter1',
                                                       pageSectionIdTitles: [], pageMeta: [:]],
                                                      [sectionTitle       : 'Chapter1', pageTitle: 'Page B', fileName: 'page-b', dirName: 'chapter1',
                                                       pageSectionIdTitles: [], pageMeta: [:]]]],
                                      [sectionTitle: 'Chapter2', dirName: 'chapter2',
                                       items       :
                                               [[sectionTitle       : 'Chapter2', pageTitle: 'Page C', fileName: 'page-c', dirName: 'chapter2',
                                                 pageSectionIdTitles: [], pageMeta: [:]]]]]

    }
}
