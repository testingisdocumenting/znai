package com.twosigma.documentation.extensions.include

import com.twosigma.documentation.extensions.PluginParams
import org.junit.Test

/**
 * @author mykola
 */
class PluginParamsTest {
    @Test
    void "should parse extra parameters as json"() {
        def params = new PluginParams("test-plugin","free-text {key1: 'value1', key2: 10}")
        assert params.opts.key1 == 'value1'
        assert params.opts.key2 == 10
    }
}
