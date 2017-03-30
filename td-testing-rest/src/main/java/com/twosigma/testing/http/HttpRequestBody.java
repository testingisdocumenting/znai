package com.twosigma.testing.http;

/**
 * @author mykola
 */
public interface HttpRequestBody {
    boolean isBinary();
    String type();
    String asString();
    default byte[] asBytes() {
        return asString().getBytes();
    }
}
