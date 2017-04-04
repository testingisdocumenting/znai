package com.twosigma.testing.documentation

/**
 * @author mykola
 */
class DocumentationContext {
    private static ThreadLocal<String> markupScenarioThreadLocal = new ThreadLocal<>()

    static void reset() {
        markupScenarioThreadLocal.set("")
    }

    static void scenario(String markupScenario) {
        markupScenarioThreadLocal.set(markupScenario)
    }

    static String getMarkupScenario() {
        return markupScenarioThreadLocal.get()
    }
}
