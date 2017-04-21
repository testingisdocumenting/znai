package com.twosigma.testing.webui.page

/**
 * @author mykola
 */
class PageObjectLoader {
    private GroovyShell groovy
    private PageObject pageObject

    PageObjectLoader(GroovyShell groovy) {
        this.groovy = groovy
        this.pageObject = new PageObject()
    }

    PageObject load(String pageScript) {
        def script = groovy.parse(pageScript)
        script.setProperty("page", this.&page)
        script.run()

        return pageObject
    }

    private void page(Closure pageDef) {
        Closure cloned = pageDef.clone() as Closure
        cloned.delegate = this
        cloned.resolveStrategy = Closure.DELEGATE_FIRST
        cloned.run()
    }

    void setProperty(String name, value) {
        pageObject.setProperty(name, value)
    }

    Object getProperty(String name) {
        pageObject.getProperty(name)
    }
}
