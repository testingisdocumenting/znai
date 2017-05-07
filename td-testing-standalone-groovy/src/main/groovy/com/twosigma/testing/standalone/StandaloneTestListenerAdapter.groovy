package com.twosigma.testing.standalone

import java.nio.file.Path

/**
 * @author mykola
 */
class StandaloneTestListenerAdapter implements StandaloneTestListener {
    @Override
    void beforeFirstTest() {
    }

    @Override
    void beforeScriptParse(Path scriptPath) {
    }

    @Override
    void beforeTestRun(StandaloneTest test) {
    }

    @Override
    void afterTestRun(StandaloneTest test) {
    }

    @Override
    void afterAllTests() {
    }
}
