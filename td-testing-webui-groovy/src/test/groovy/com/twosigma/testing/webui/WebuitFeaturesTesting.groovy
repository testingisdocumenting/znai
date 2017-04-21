package com.twosigma.testing.webui

import com.twosigma.testing.http.testserver.TestServer
import com.twosigma.testing.webui.cli.WebUiTestCliApp
import com.twosigma.utils.ResourceUtils
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

/**
 * @author mykola
 */
class WebuitFeaturesTesting {
    private static final int testServerPort = 8180
    private static TestServer testServer

    @BeforeClass
    static void init() {
        testServer = new TestServer()
        testServer.registerGet("/search", new TestServerHtmlResponse(ResourceUtils.textContent("search.html")))
        testServer.start(testServerPort)
    }

    @AfterClass
    static void cleanup() {
        testServer.stop()
    }

    @Test
    void "opens website"() {
        runCli("examples/Search.groovy")
    }

    static void runCli(String testFileName) {
        def cliApp = new WebUiTestCliApp("--url=http://localhost:" + testServerPort, "--file=" + testFileName)
        cliApp.start()
    }
}
