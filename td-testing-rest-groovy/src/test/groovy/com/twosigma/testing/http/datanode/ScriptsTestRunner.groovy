package com.twosigma.testing.http.datanode

import com.twosigma.testing.http.runner.HttpTestRunner
import com.twosigma.testing.http.testserver.TestServer
import com.twosigma.testing.http.testserver.TestServerResponseEcho
import com.twosigma.utils.ResourceUtils

/**
 * @author mykola
 */
class ScriptsTestRunner {
    private TestServer testServer = new TestServer()

    void before() {
        testServer.start(8070)
        testServer.registerPost("/echo", new TestServerResponseEcho())
    }

    void after() {
        testServer.stop()
    }

    void run(String script) {
        def testRunner = new HttpTestRunner(new Configuration(ResourceUtils.textContent("config.groovy"), "dev"))
        testRunner.register("test-name", script)

        testRunner.run()
    }
}
