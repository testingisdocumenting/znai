package com.twosigma.testing

import com.twosigma.testing.http.testserver.TestServer
import com.twosigma.testing.http.testserver.TestServerJsonResponse
import com.twosigma.testing.http.testserver.TestServerResponse
import com.twosigma.testing.webtau.TestServerHtmlResponse
import com.twosigma.utils.JsonUtils
import com.twosigma.utils.ResourceUtils

/**
 * @author mykola
 */
class WebTauFeaturesTestServer {
    private TestServer testServer

    WebTauFeaturesTestServer() {
        testServer = new TestServer()
        testServer.registerGet("/search", new TestServerHtmlResponse(ResourceUtils.textContent("search.html")))
        testServer.registerGet("/finders-and-filters", new TestServerHtmlResponse(ResourceUtils.textContent("finders-and-filters.html")))
        testServer.registerGet("/with-cookies", new TestServerHtmlResponse(ResourceUtils.textContent("cookies.html")))
        testServer.registerGet("/weather", new TestServerJsonResponse("{\"temperature\": 20}"))

        testServer.registerPost("/employee", json([id: 'id-generated-2']))
        testServer.registerGet("/employee/id-generated-2", json([firstName: 'FN', lastName: 'LN']))
    }

    void start(int port) {
        testServer.start(port)
    }

    void stop() {
        testServer.stop()
    }

    static void main(String[] args) {
        def testServer = new WebTauFeaturesTestServer()
        testServer.start(8180)
    }

    private static TestServerResponse json(Map response) {
        return new TestServerJsonResponse(JsonUtils.serialize(response))
    }
}
