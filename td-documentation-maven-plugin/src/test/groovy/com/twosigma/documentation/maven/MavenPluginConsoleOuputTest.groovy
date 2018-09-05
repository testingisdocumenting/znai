package com.twosigma.documentation.maven

import com.twosigma.console.ansi.Color
import org.junit.Test

class MavenPluginConsoleOutputTest {
    @Test
    void "should ignore colours and invoke toString"() {
        def output = MavenPluginConsoleOuput.concatIgnoringColours("foo", Color.RED, "bar", 123)
        output.should == 'foobar123'
    }
}
