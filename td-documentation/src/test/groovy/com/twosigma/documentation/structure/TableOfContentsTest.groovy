package com.twosigma.documentation.structure

import com.twosigma.documentation.parser.PageSectionIdTitle
import org.junit.Test

/**
 * @author mykola
 */
class TableOfContentsTest {
    @Test
    void "should create top level TOC from nested text structure"() {
        def toc = TableOfContents.fromNestedText("""
chapter1
    page-a
    page-b
chapter2
    page-c""")

        toc.toListOfMaps().should == [[sectionTitle: 'Chapter1', dirName: 'chapter1',
                                       items       : [[sectionTitle       : 'Chapter1', pageTitle: 'Page A', fileName: 'page-a', dirName: 'chapter1',
                                                       pageSectionIdTitles: []],
                                                      [sectionTitle       : 'Chapter1', pageTitle: 'Page B', fileName: 'page-b', dirName: 'chapter1',
                                                       pageSectionIdTitles: []]]],
                                      [sectionTitle: 'Chapter2', dirName: 'chapter2',
                                       items       :
                                               [[sectionTitle       : 'Chapter2', pageTitle: 'Page C', fileName: 'page-c', dirName: 'chapter2',
                                                 pageSectionIdTitles: []]]]]

    }

    @Test
    void "should know which toc item is defined and which is not"() {
        def toc = new TableOfContents()
        toc.addTocItem("chapter1", "page-a")
        toc.addTocItem("chapter1", "page-b")

        toc.contains("chapter1", "page-a", "").should == true
        toc.contains("chapter1", "page-c", "").should == false

        toc.contains("chapter1", "page-a", "page section title").should == false
        def tocItem = toc.findTocItem("chapter1", "page-a")

        def pageSection1 = new PageSectionIdTitle("page section title")
        def pageSection2 = new PageSectionIdTitle("another title")
        tocItem.setPageSectionIdTitles([pageSection1, pageSection2])

        toc.contains("chapter1", "page-a", pageSection1.id).should == true
        toc.contains("chapter1", "page-a", "another title").should == false
    }
}
