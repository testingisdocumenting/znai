package com.twosigma.testing.webtau.cli

import com.twosigma.testing.standalone.GroovyStandaloneEngine
import com.twosigma.testing.webtau.cfg.WebTauConfig
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class WebTauTestCliConfigTest {
    private static def groovy = GroovyStandaloneEngine.createWithoutDelegating(Paths.get(""), [])
    private static final cfg = WebTauConfig.INSTANCE

    @Test
    void "should use default environment values when evn is not specified"() {
        def cliConfig = new WebTauTestCliConfig("--config=src/test/resources/test.cfg")
        cliConfig.parseConfig(groovy)

        cfg.baseUrl.should == "http://localhost:8180"
    }

    @Test
    void "should use environment specific values when evn is specified"() {
        def cliConfig = new WebTauTestCliConfig("--config=src/test/resources/test.cfg", "--env=dev")
        cliConfig.parseConfig(groovy)

        cfg.baseUrl.should == "http://dev.host:8080"
    }
}
