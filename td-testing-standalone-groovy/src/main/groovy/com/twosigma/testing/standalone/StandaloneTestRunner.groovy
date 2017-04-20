package com.twosigma.testing.standalone

import com.twosigma.utils.FileUtils
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import java.nio.file.Path

/**
 * @author mykola
 */
class StandaloneTestRunner {
    private List<String> staticImports = []
    private List<StandaloneTest> tests = []

    private Path currentTestPath
    private GroovyShell groovy
    private StandaloneTestListener testListener

    StandaloneTestRunner(List<String> staticImports, StandaloneTestListener testListener) {
        this.staticImports = staticImports
        this.testListener = testListener
        this.groovy = prepareGroovy()
    }

    void process(Path scriptPath) {
        process(scriptPath, FileUtils.fileTextContent(scriptPath))
    }

    void process(Path scriptPath, String scriptBody) {
        currentTestPath = scriptPath
        def script = groovy.parse(scriptBody)
        script.setProperty("scenario", this.&scenario)

        script.run()
    }

    List<StandaloneTest> getTests() {
        return tests
    }

    int getNumberOfPassed() {
        return tests.count { it.isPassed() }
    }

    int getNumberOfFailed() {
        return tests.count { it.isFailed() }
    }

    int getNumberOfErrored() {
        return tests.count { it.hasError() }
    }

    void runTests() {
        testListener.beforeFirstTest()
        tests.each {
            it.run()
            testListener.afterTest(it)
        }
    }

    private void scenario(String description, Closure code) {
        def test = new StandaloneTest(currentTestPath, description, code)
        tests.add(test)
    }

    private GroovyShell prepareGroovy() {
        def imports = new ImportCustomizer()
        def fullListOfStatics = staticImports + [StandaloneTestRunner.canonicalName]
        println fullListOfStatics

        fullListOfStatics.forEach { imports.addStaticStars(it) }

        def compilerCfg = new CompilerConfiguration()
        compilerCfg.addCompilationCustomizers(imports)

        return new GroovyShell(compilerCfg)
    }
}
