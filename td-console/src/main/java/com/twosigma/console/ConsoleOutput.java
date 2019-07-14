package com.twosigma.console;

public interface ConsoleOutput {
    void out(Object... styleOrValues);
    void err(Object... styleOrValues);
}
