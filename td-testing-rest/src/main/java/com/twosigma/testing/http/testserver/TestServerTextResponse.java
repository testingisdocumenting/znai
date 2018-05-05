package com.twosigma.testing.http.testserver;

/**
 * @author mykola
 */
public class TestServerTextResponse implements TestServerResponse {
    private String response;

    public TestServerTextResponse(String response) {
        this.response = response;
    }

    @Override
    public String responseBody(TestServerRequest request) {
        return response;
    }

    @Override
    public String responseType(TestServerRequest request) {
        return "text/html";
    }
}
