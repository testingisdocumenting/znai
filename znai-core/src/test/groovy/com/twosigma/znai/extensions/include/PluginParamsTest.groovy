package com.twosigma.znai.extensions.include

import com.twosigma.znai.extensions.PluginParams
import org.junit.Test

class PluginParamsTest {
    @Test
    void "should parse extra parameters as json"() {
        def params = new PluginParams("test-plugin","free-text {key1: 'value1', key2: 10}")
        assert params.opts.key1 == 'value1'
        assert params.opts.key2 == 10
    }

    @Test
    void "should auto convert rightSide value to meta-rightSide"() {
        def opts = new PluginParams('file', [rightSide: true]).opts.toMap()
        opts.get('rightSide').should == null
        opts.get('meta').should == [rightSide: true]
    }

    @Test
    void "should keep rightSide value as is if plugin is include meta"() {
        def opts = new PluginParams('meta', [rightSide: true]).opts.toMap()
        opts.get('rightSide').should == true
        opts.get('meta').should == null
    }
}
