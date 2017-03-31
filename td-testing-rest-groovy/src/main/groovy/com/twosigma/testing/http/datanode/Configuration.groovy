package com.twosigma.testing.http.datanode

import java.nio.file.Path

/**
 * @author mykola
 */
class Configuration {
    private ConfigSlurper configSlurper
    private ConfigObject configObject

    Configuration(Path configPath, String env) {
        configSlurper = new ConfigSlurper(env)
        configObject = configSlurper.parse(configPath.toFile().toURL())
    }

    String getBaseUrl() {
        return configObject.url
    }
}
