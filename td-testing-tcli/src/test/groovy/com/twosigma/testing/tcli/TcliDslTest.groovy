package com.twosigma.testing.tcli

import org.junit.Test

/**
 * @author mykola
 */
class TcliDslTest {
    @Test
    void "runCli closure should have access to the output"() {
        TcliDsl.runCli("ls pom.xml") {
            out.should == "pom.xml\n"
        }
    }
}
