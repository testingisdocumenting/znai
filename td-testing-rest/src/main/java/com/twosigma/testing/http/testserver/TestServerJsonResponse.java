package com.twosigma.testing.http.testserver;

/**
 * @author mykola
 */
public class TestServerJsonResponse implements TestServerResponse {
    private String response;

    public TestServerJsonResponse(String response) {
        this.response = response;
    }

    @Override
    public String responseBody(TestServerRequest request) {
        return response;
    }

    @Override
    public String responseType(TestServerRequest request) {
        return "application/json";
    }
}
