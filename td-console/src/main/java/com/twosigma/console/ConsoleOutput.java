package com.twosigma.console;

/**
 * @author mykola
 */
public interface ConsoleOutput {
    void out(Object... styleOrValue);
    void err(Object... styleOrValue);
}
