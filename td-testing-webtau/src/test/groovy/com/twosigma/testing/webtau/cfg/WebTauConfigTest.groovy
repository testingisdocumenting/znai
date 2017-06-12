package com.twosigma.testing.webtau.cfg

import org.junit.Test

/**
 * @author mykola
 */
class WebTauConfigTest {
    @Test
    void "inits config values from env vars and overrides them from system properties"() {
        System.setProperty('url', 'test-base-url')
        WebTauConfig cfg = new WebTauConfig()
        cfg.getBaseUrl().should == 'test-base-url'
    }

    @Test
    void "allows to manually override base url"() {
        System.setProperty('url', 'original-base-url')

        WebTauConfig cfg = new WebTauConfig()
        cfg.setBaseUrl('new-url')
        
        cfg.getBaseUrl().should == "new-url"
        cfg.baseUrlConfigValue.getSources().should == ["manual", "system property"]
    }
}
