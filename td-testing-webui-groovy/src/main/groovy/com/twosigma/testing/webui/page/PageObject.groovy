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

    @Override
    String toString() {
        return definitions.toString()
    }
}
