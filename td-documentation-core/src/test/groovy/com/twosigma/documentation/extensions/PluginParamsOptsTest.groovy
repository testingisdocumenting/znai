package com.twosigma.documentation.extensions

import org.junit.Test

/**
 * @author mykola
 */
class PluginParamsOptsTest {
    @Test
    void "should auto convert singular values into a list"() {
        def opts = new PluginParamsOpts([highlight: 1, params: ['a', 'b']])
        opts.getList("highlight").should == [1]
        opts.getList("params").should == ['a', 'b']
    }

    @Test
    void "should auto convert rightSide value to meta-rightSide"() {
        def opts = new PluginParamsOpts([rightSide: true])
        opts.toMap().get('meta').should == [rightSide: true]
    }
}
