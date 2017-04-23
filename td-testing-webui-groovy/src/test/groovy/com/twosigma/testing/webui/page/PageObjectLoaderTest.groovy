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
    submit = searchBox
}"""
        def object = pageObjectLoader.load(pageDef)
        object.searchBox.should == 'search-box'
        object.submit.should == 'search-box'
    }
}
