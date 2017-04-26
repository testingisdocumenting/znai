package com.twosigma.testing

import com.twosigma.testing.http.testserver.TestServer
import com.twosigma.testing.webui.TestServerHtmlResponse
import com.twosigma.utils.ResourceUtils

/**
 * @author mykola
 */
class WebuitFeaturesTestServer {
    private TestServer testServer

    WebuitFeaturesTestServer() {
        testServer = new TestServer()
        testServer.registerGet("/search", new TestServerHtmlResponse(ResourceUtils.textContent("search.html")))
    }

    void start(int port) {
        testServer.start(port)
    }

    void stop() {
        testServer.stop()
    }

    static void main(String[] args) {
        def testServer = new WebuitFeaturesTestServer()
        testServer.start(8180)
    }
}
