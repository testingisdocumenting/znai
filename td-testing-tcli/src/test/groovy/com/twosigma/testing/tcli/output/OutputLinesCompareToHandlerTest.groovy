package com.twosigma.testing.tcli.output

import org.junit.Test

/**
 * @author mykola
 */
class OutputLinesCompareToHandlerTest {
    @Test
    void "should mark lines that passed assertion"() {
        def lines = new OutputLines("out", "line1\nline2\nline3\nline4")
        lines.should == 'line2'
        lines.should == 'line4'

        lines.getCheckedLineIdx().should == [1, 3]
    }

    @Test
    void "should handle not equals"() {
        def lines = new OutputLines("out", "line1\nline2\nline3\nline4")
        lines.shouldNot == 'foo'
    }
}
