package com.twosigma.console;

/**
 * @author mykola
 */
public enum FontStyle {
    NORMAL("\u001B[0m"),
    BOLD("\u001B[1m");

    private final String code;

    FontStyle(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
