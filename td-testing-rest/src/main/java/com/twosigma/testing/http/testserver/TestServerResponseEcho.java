package com.twosigma.testing.http.testserver;

/**
 * @author mykola
 */
public class TestServerResponseEcho implements TestServerResponse {
    @Override
    public String generate(final TestServerRequest request) {
        return request.getRequestBody();
    }
}
