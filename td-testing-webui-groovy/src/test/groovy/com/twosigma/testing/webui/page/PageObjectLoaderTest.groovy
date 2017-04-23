package com.twosigma.testing.webui.page

import org.codehaus.groovy.control.CompilerConfiguration
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
        def compilerCfg = new CompilerConfiguration()
        compilerCfg.scriptBaseClass = DelegatingScript.class.name
        groovyShell = new GroovyShell(compilerCfg)
        pageObjectLoader = new PageObjectLoader(groovyShell)
    }

    @Test
    void "should handle properties assignment"() {
       def pageDef = """
page {
    searchBox = 'search-box'
    enterButton = searchBox
    about = 'about'
}"""
        def object = pageObjectLoader.load(pageDef)
        object.searchBox.should == 'search-box'
        object.enterButton.should == 'search-box'
        object.about.should == 'about'
    }

    @Test
    void "should handle actions assignment"() {
       def pageDef = """
page {
    submit = { value, two -> return value + two }
}"""
        def object = pageObjectLoader.load(pageDef)
        object.submit("t1", "o2").should == 't1o2'
    }
}
