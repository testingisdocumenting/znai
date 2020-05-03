package org.testingisdocumenting.znai.html

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.contain

class HtmlPageTest {
    @Test
    void "should add custom render to body"() {
        def page = new HtmlPage()
        page.addToBody { 'custom body block' }

        def rendered = page.render('my-doc')
        rendered.should contain('custom body block')
    }

    @Test
    void "should add theme controller to body"() {
        def page = new HtmlPage()
        def rendered = page.render('my-doc')
        rendered.should contain('var themeNameKey = \'znaiTheme\';')
    }

    @Test
    void "should add custom render to head"() {
        def page = new HtmlPage()
        page.addToHead { 'custom head block' }

        def rendered = page.render('my-doc')
        rendered.should contain('custom head block\n</head>')
    }
}
