package com.twosigma.documentation.search

import com.twosigma.documentation.structure.TocItem
import org.junit.Test

class PageSearchEntriesTest {
    @Test
    void "should generate list representation of entries for local search indexer"() {
        def searchEntries = new PageSearchEntries(
                new TocItem('dir-name', 'file-name'),
                [new PageSearchEntry('section one', SearchScore.STANDARD.text('hello world')),
                 new PageSearchEntry('section two', SearchScore.STANDARD.text('how is the weather')),
                ])

        searchEntries.toListOfLists().should == [
                ['dir-name@@file-name@@section-one', 'Dir Name', 'File Name', 'section one', 'hello world'],
                ['dir-name@@file-name@@section-two', 'Dir Name', 'File Name', 'section two', 'how is the weather']]
    }
}
