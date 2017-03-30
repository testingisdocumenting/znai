package com.twosigma.testing.http.testserver;

/**
 * @author mykola
 */
public class TestServerResponseConstant implements TestServerResponse {
    private String response;

    public TestServerResponseConstant(String response) {
        this.response = response;
    }

    @Override
    public String generate(final TestServerRequest request) {
        return response;
    }
}
