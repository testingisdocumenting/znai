package com.twosigma.console.ansi;

import com.twosigma.console.ConsoleOutput;

/**
 * @author mykola
 */
public class AnsiConsoleOutput implements ConsoleOutput {
    @Override
    public void out(Object... styleOrValues) {
        System.out.println(new AutoResetAnsiString(styleOrValues));
    }

    @Override
    public void err(Object... styleOrValues) {
        System.err.println(new AutoResetAnsiString(styleOrValues));
    }
}
