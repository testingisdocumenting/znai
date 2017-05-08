package com.twosigma.testing.http.cli

/**
 * @author mykola
 */
class HttpTestConfig {
    private ConfigSlurper configSlurper
    private ConfigObject configObject

    HttpTestConfig(String script, String env) {
        configSlurper = new ConfigSlurper(env)
        configObject = configSlurper.parse(script)
    }

    String getBaseUrl() {
        return configObject.url
    }
}
