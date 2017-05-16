package com.twosigma.testing.webtau

import com.twosigma.testing.http.testserver.TestServerRequest
import com.twosigma.testing.http.testserver.TestServerResponse

/**
 * @author mykola
 */
class TestServerHtmlResponse implements TestServerResponse {
    private String response

    TestServerHtmlResponse(String response) {
        this.response = response
    }

    @Override
    String responseBody(final TestServerRequest request) {
        return response
    }

    @Override
    String responseType(TestServerRequest request) {
        return "text/html"
    }
}
