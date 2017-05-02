package com.twosigma.documentation.extensions.include

import org.junit.Test

/**
 * @author mykola
 */
class IncludeParamsTest {
    @Test
    void "should parse extra parameters as json"() {
        def params = new IncludeParams("test-plugin","free-text {key1: 'value1', key2: 10}")
        assert params.opts.key1 == 'value1'
        assert params.opts.key2 == 10
    }
}
