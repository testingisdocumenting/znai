package com.twosigma.testing.http;

/**
 * @author mykola
 */
public interface HttpTestListener {
    void afterValidation(HttpValidationResult validationResult);
}
