package com.twosigma.testing.tcli.process

import org.junit.Test

class CliProcessTest {
    @Test
    void "should capture output of a process"() {
        def cliProcess = new CliProcess("ls")
        cliProcess.start()

        assert cliProcess.out.toString().contains("src")
    }
}
