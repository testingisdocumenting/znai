package com.twosigma.znai.extensions

import org.junit.Test

class PluginParamsOptsTest {
    @Test
    void "should auto convert singular values into a list"() {
        def opts = new PluginParamsOpts([highlight: 1, params: ['a', 'b']])
        opts.getList("highlight").should == [1]
        opts.getList("params").should == ['a', 'b']
    }
}
