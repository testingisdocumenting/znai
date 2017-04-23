package com.twosigma.testing.standalone

import java.nio.file.Path

/**
 * @author mykola
 */
interface StandaloneTestListener {
    void beforeFirstTest()
    void beforeScriptParse(Path scriptPath)
    void beforeTestRun(StandaloneTest test)
    void afterTestRun(StandaloneTest test)
}
