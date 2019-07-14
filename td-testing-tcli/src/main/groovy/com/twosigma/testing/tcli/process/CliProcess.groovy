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
