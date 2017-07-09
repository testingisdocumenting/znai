package com.twosigma.testing.tcli

import com.twosigma.testing.standalone.StandaloneTestRunner
import com.twosigma.testing.tcli.output.OutputLines
import com.twosigma.testing.tcli.process.CliProcess

/**
 * @author mykola
 */
class TcliDsl {
    private static StandaloneTestRunner testRunner
    private final CliProcess cliProcess

    TcliDsl(CliProcess cliProcess) {
        this.cliProcess = cliProcess
    }

    static void initWithTestRunner(StandaloneTestRunner testRunner) {
        this.testRunner = testRunner
    }

    static void scenario(String description, Closure code) {
        testRunner.scenario(description, code)
    }

    static void runCli(String command, Closure dslCode) {
        def process = new CliProcess(command)
        process.start()

        Closure clonedDslCode = dslCode.clone() as Closure
        clonedDslCode.delegate = new TcliDsl(process)
        clonedDslCode.run()
    }

    String getOut() {
        return cliProcess.out
    }

    OutputLines getLine() {
        return new OutputLines("<line of std output>", cliProcess.out.toString())
    }
}
