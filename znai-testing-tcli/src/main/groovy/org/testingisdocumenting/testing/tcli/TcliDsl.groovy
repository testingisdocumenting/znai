/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.testing.tcli

import com.twosigma.testing.tcli.output.OutputLines
import com.twosigma.testing.tcli.process.CliProcess
import com.twosigma.webtau.runner.standalone.StandaloneTestRunner

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
