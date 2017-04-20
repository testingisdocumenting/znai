package com.twosigma.testing.webui

import com.twosigma.testing.http.testserver.TestServer
import com.twosigma.testing.webui.cli.WebUiTestCliApp
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

/**
 * @author mykola
 */
abstract class TestOfWebuit {
    private static TestServer testServer

    @BeforeClass
    static void init() {
        testServer = new TestServer()
    }

    @AfterClass
    static void cleanup() {

    }

    @Test
    void "should run provided script and validate expected artifacts"() {
        def args = cliParams().collect {k, v -> "--${k}=${v}".toString() }
        def cliApp = new WebUiTestCliApp(args.toArray(new String[0]))

        println "starting cli with args: $args"
        cliApp.start()
    }

    abstract Map cliParams()
}
