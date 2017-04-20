package com.twosigma.testing.http.testserver;

/**
 * @author mykola
 */
public interface TestServerResponse {
    String responseBody(TestServerRequest request);
    String responseType(TestServerRequest request);
}
