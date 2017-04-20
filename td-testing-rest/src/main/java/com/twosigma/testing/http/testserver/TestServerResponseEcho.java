package com.twosigma.testing.http.testserver;

/**
 * @author mykola
 */
public class TestServerResponseEcho implements TestServerResponse {
    @Override
    public String responseBody(final TestServerRequest request) {
        return request.getRequestBody();
    }

    @Override
    public String responseType(TestServerRequest request) {
        return request.getRequestType();
    }
}
