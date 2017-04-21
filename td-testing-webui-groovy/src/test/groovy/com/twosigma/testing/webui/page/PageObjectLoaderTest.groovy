package com.twosigma.testing.webui.page

import org.junit.Before
import org.junit.Test

/**
 * @author mykola
 */
class PageObjectLoaderTest {
    GroovyShell groovyShell
    PageObjectLoader pageObjectLoader

    @Before
    void init() {
        groovyShell = new GroovyShell()
        pageObjectLoader = new PageObjectLoader(groovyShell)
    }

    @Test
    void "should handle properties assignment"() {
       def pageDef = """
page {
    searchBox = 'search-box'
    submit = searchBox
}"""
        def object = pageObjectLoader.load(pageDef)
        object.searchBox.should == 'search-box'
        object.submit.should == 'search-box'
    }
}
