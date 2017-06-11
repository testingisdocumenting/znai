package com.twosigma.testing.webtau.page;

/**
 * @author mykola
 */
public class ElementNotFoundException extends AssertionError {
    public ElementNotFoundException(String message) {
        super(message);
    }
}
