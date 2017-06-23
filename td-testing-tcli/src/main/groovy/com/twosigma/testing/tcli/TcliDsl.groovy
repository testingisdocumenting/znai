package com.twosigma.testing.tcli

import com.twosigma.testing.standalone.StandaloneTestRunner

/**
 * @author mykola
 */
class TcliDsl {
    private static StandaloneTestRunner testRunner

    static void initWithTestRunner(StandaloneTestRunner testRunner) {
        this.testRunner = testRunner
    }

    static void scenario(String description, Closure code) {
        testRunner.scenario(description, code)
    }
}
