package com.twosigma.testing.webtau.cli

import com.twosigma.testing.webtau.cfg.WebTauConfig
import org.junit.Test

/**
 * @author mykola
 */
class WebUiTestCliConfigTest {
    private static final cfg = WebTauConfig.INSTANCE

    @Test
    void "should use default environment values when evn is not specified"() {
        def cliConfig = new WebUiTestCliConfig("--config=src/test/resources/test.cfg")
        cfg.baseUrl.should == "http://localhost:8180"
    }

    @Test
    void "should use environment specific values when evn is specified"() {
        def cliConfig = new WebUiTestCliConfig("--config=src/test/resources/test.cfg", "--env=dev")
        cfg.baseUrl.should == "http://dev.host:8080"
    }
}
