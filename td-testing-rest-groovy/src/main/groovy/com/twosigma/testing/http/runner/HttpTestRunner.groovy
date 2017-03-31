package com.twosigma.testing.http.runner

import com.twosigma.testing.http.HttpTestListener
import com.twosigma.testing.http.HttpTestListeners
import com.twosigma.testing.http.HttpUrl
import com.twosigma.testing.http.HttpValidationResult
import com.twosigma.testing.http.config.HttpConfiguration
import com.twosigma.testing.http.config.HttpConfigurations
import com.twosigma.testing.http.datacoverage.DataNodeToMapWithChecksConverter
import com.twosigma.testing.http.datanode.Configuration
import com.twosigma.testing.http.render.DataNodeRenderer
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class HttpTestRunner implements HttpTestListener, HttpConfiguration {
    private GroovyShell groovy
    private Configuration configuration

    private List<HttpTest> tests = []
    private HttpTest currentTest

    HttpTestRunner() {
        groovy = prepareGroovy()
        configuration = new Configuration(Paths.get("config.groovy"), "dev")

        HttpTestListeners.add(this)
        HttpConfigurations.add(this)
    }

    void register(Path testPath) {
        def script = groovy.parse(testPath.text)
        tests.add(new HttpTest(testPath.fileName.toString(), script))
    }

    void run() {
        tests.forEach { test ->
            currentTest = test
            test.run()
        }
    }

    private static GroovyShell prepareGroovy() {
        def imports = new ImportCustomizer()
        imports.addStaticStars("com.twosigma.testing.http.Http")

        def compilerCfg = new CompilerConfiguration()
        compilerCfg.addCompilationCustomizers(imports)

        return new GroovyShell(compilerCfg)
    }

    @Override
    void afterValidation(final HttpValidationResult validationResult) {
        currentTest.afterValidation(validationResult)
    }

    @Override
    String fullUrl(final String url) {
        return HttpUrl.isFull(url) ? url : HttpUrl.concat(configuration.baseUrl, url)
    }
}
