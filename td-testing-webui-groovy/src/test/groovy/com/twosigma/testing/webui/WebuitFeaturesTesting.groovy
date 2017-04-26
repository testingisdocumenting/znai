package com.twosigma.testing.webui

import com.twosigma.testing.WebuitFeaturesTestServer
import com.twosigma.testing.webui.cli.WebUiTestCliApp
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

/**
 * @author mykola
 */
class WebuitFeaturesTesting {
    private static final int testServerPort = 8180
    private static WebuitFeaturesTestServer testServer

    @BeforeClass
    static void init() {
        System.setProperty("url", "http://localhost:" + testServerPort)

        testServer = new WebuitFeaturesTestServer()
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
