package com.twosigma.testing.standalone.report

import com.twosigma.utils.TraceUtils

/**
 * @author mykola
 */
class GroovyStackTraceUtils {
    private static List<String> libPrefixes = [
            "sun.reflect",
            "sun.net.",
            "sun.security.",
            "java.net.",
            "com.sun.",
            "java.base/",
            "com.twosigma", // TODO limit to webtau
            "org.codehaus.groovy",
            "org.junit",
            "com.intellij",
            "java.lang",
            "groovy.lang"
    ]

    private GroovyStackTraceUtils() {
    }

    static String renderStackTraceWithoutLibCalls(Throwable t) {
        return filterStackTrace(t) { !isStandardCall(it) && !isMoreMessage(it) }
    }

    static String fullCauseMessage(Throwable t) {
        return filterStackTrace(t) { !isAtLine(it) && !isMoreMessage(it) }
    }

    static String filterStackTrace(Throwable t, Closure filter) {
        def stackTrace = TraceUtils.stackTrace(t)
        def lines = stackTrace.split("\n")

        return lines[0] + "\n" +
                lines[1..lines.length - 1].findAll(filter).join("\n")
    }

    private static boolean isStandardCall(String stackTraceLine) {
        return libPrefixes.any { isAtLine(stackTraceLine) && stackTraceLine.contains(it) }
    }

    private static boolean isAtLine(String stackTraceLine) {
        return stackTraceLine.trim().startsWith('at')
    }

    private static boolean isMoreMessage(String stackTraceLine) {
        return stackTraceLine.trim().startsWith('...')
    }
}
