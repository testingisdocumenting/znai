package com.twosigma.console;

/**
 * @author mykola
 */
public interface ConsoleOutput {
    void out(Object... styleOrValues);
    void err(Object... styleOrValues);
}
