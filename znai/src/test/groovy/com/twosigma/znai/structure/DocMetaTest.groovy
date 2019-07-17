package com.twosigma.znai.structure

import org.junit.Test

class DocMetaTest {
    @Test
    void "should create a new doc meta instance based on existing data and new json"() {
        def existing = new DocMeta([title: 'old title', type: 'old type'])
        existing.id = 'doc-id'
        existing.previewEnabled = true

        def clone = existing.cloneWithNewJson("""
{"title": "new title", 
"type": "guide"}
""")

        clone.id.should == 'doc-id'
        clone.previewEnabled.should == true
        clone.title.should == 'new title'
        clone.type.should == 'guide'
    }
}
