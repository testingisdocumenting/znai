package com.twosigma.testing.http.testserver;

/**
 * @author mykola
 */
public interface TestServerResponse {
    String generate(TestServerRequest request);
}
