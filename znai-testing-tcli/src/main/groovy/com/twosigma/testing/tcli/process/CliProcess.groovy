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

package com.twosigma.testing.tcli.process

class CliProcess {
    private String cmd
    private StringBuilder out
    private StringBuilder err
    private int exitCode

    CliProcess(String cmd) {
        this.cmd = cmd
        this.out = new StringBuilder()
        this.err = new StringBuilder()
    }

    void start() {
        def process = cmd.execute()
        process.waitForProcessOutput(new OutputCapture(out), new OutputCapture(err))

        exitCode = process.exitValue()
    }

    StringBuilder getOut() {
        return out
    }

    StringBuilder getErr() {
        return err
    }

    int getExitCode() {
        return exitCode
    }

    private static class OutputCapture implements Appendable {
        private StringBuilder builder

        OutputCapture(StringBuilder builder) {
            this.builder = builder
        }

        @Override
        Appendable append(CharSequence csq) throws IOException {
            builder.append(csq)
            return this
        }

        @Override
        Appendable append(CharSequence csq, int start, int end) throws IOException {
            throw new UnsupportedOperationException()
        }

        @Override
        Appendable append(char c) throws IOException {
            throw new UnsupportedOperationException()
        }
    }
}
