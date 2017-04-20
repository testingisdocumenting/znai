package com.twosigma.testing.standalone

/**
 * @author mykola
 */
interface StandaloneTestListener {
    void beforeFirstTest()
    void afterTest(StandaloneTest test)
}
