package com.twosigma.testing.standalone.report

import com.twosigma.utils.TraceUtils

/**
 * @author mykola
 */
class GroovyStackTraceUtils {
    private static List<String> libPrefixes = [
            "sun.reflect",
            "com.twosigma",
            "org.codehaus.groovy",
            "org.junit",
            "com.intellij",
            "java.lang",
            "groovy.lang"
    ]

    private GroovyStackTraceUtils() {
    }

    static String renderStackTraceWithoutLibCalls(Throwable t) {
        return removeLibsCalls(TraceUtils.stackTrace(t))
    }

    static String removeLibsCalls(String stackTrace) {
        def lines = stackTrace.split("\n")

        return lines[0] + "\n" +
                lines[1..lines.length - 1].findAll { !isStandardCall(it) }.join("\n")
    }

    private static boolean isStandardCall(String stackTraceLine) {
        return libPrefixes.any { stackTraceLine.contains(it) }
    }
}
