package com.twosigma.testing.standalone

import com.twosigma.utils.ServiceLoaderUtils

import java.nio.file.Path

/**
 * @author mykola
 */
class StandaloneTestListeners {
    private static List<StandaloneTestListener> listeners = ServiceLoaderUtils.load(StandaloneTestListener)

    static void add(StandaloneTestListener listener) {
        listeners.add(listener)
    }

    static void remove(StandaloneTestListener listener) {
        listeners.remove(listener)
    }

    static void beforeFirstTest() {
        listeners.each { it.beforeFirstTest() }
    }

    static void beforeScriptParse(Path scriptPath) {
        listeners.each { it.beforeScriptParse(scriptPath) }
    }

    static void beforeTestRun(StandaloneTest test) {
        listeners.each { it.beforeTestRun(test) }
    }

    static void afterTestRun(StandaloneTest test) {
        listeners.each { it.afterTestRun(test) }
    }

    static void afterAllTests() {
        listeners.each { it.afterAllTests() }
    }
}
