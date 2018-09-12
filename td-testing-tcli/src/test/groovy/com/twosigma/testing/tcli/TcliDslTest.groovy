package com.twosigma.testing.tcli

import org.junit.Ignore
import org.junit.Test

/**
 * @author mykola
 */
class TcliDslTest {
    @Ignore("karol")
    @Test
    void "runCli closure should have access to any line through line"() {
        TcliDsl.runCli("ls") {
            line.should == "pom.xml"
            line.shouldNot == "pom1.xml"
        }
    }
}
