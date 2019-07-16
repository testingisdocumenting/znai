package com.twosigma.testing.tcli

import org.junit.Test

class TcliDslTest {
    @Test
    void "runCli closure should have access to any line through line"() {
        TcliDsl.runCli("ls") {
            line.should == "pom.xml"
            line.shouldNot == "pom1.xml"
        }
    }
}
