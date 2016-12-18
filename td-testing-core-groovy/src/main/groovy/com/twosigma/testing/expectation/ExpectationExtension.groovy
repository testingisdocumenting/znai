package com.twosigma.testing.expectation
/**
 * @author mykola
 */
class ExpectationExtension {
    static Should getShould(actual) {
        return new Should(actual)
    }
}
