package com.twosigma.testing.http.datanode

import java.nio.file.Path

/**
 * @author mykola
 */
class Configuration {
    private ConfigSlurper configSlurper
    private ConfigObject configObject

    Configuration(String script, String env) {
        configSlurper = new ConfigSlurper(env)
        configObject = configSlurper.parse(script)
    }

    String getBaseUrl() {
        return configObject.url
    }
}
