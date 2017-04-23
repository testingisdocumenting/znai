package com.twosigma.testing.webui.page

import com.twosigma.utils.FileUtils

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class PageObjectLoader {
    private GroovyShell groovy

    private ThreadLocal<PageObject> pageObject = new ThreadLocal<>()
    private ThreadLocal<Path> currentScriptPath = new ThreadLocal<>()

    PageObjectLoader(GroovyShell groovy) {
        this.groovy = groovy
    }

    PageObject load(String pageScript) {
        def script = groovy.parse(pageScript)
        script.setDelegate(this)

        pageObject.set(new PageObject())
        script.run()
        return pageObject.get()
    }

    void page(Closure pageDef) {
        Closure cloned = pageDef.clone() as Closure
        cloned.delegate = this
        cloned.resolveStrategy = Closure.DELEGATE_FIRST
        cloned.run()
    }

    PageObject page(String pageName) {
        def pageObjectLoader = new PageObjectLoader(groovy)
        return pageObjectLoader.load(FileUtils.fileTextContent(pagePath(pageName)))
    }

    void setProperty(String name, value) {
        if (name == "currentScriptPath") {
            this.@currentScriptPath.set(value)
        } else {
            pageObject.get().setProperty(name, value)
        }
    }

    Object getProperty(String name) {
        pageObject.get().getProperty(name)
    }

    private Path pagePath(String pageName) {
        def fileName = pageName + ".groovy"
        def rootBased = Paths.get(fileName).toAbsolutePath()
        def relativeToTest = currentScriptPath.get().toAbsolutePath().parent.resolve(fileName)

        return FileUtils.existingPathOrThrow(rootBased, relativeToTest)
    }
}
