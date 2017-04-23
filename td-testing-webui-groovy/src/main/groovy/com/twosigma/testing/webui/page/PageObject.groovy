package com.twosigma.testing.webui.page

/**
 * @author mykola
 */
class PageObject {
    private Map<String, Object> definitions = [:]

    void setProperty(String key, value) {
        definitions.put(key, value)
    }

    def getProperty(String key) {
        return definitions.get(key)
    }

    Object invokeMethod(String name, Object args) {
        Closure method = getProperty(name) as Closure
        return method.curry(args).call()
    }

    @Override
    String toString() {
        return definitions.toString()
    }
}
