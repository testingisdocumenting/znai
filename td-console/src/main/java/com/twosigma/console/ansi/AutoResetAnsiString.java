package com.twosigma.console.ansi;

/**
 * @author mykola
 */
public class AutoResetAnsiString {
    private StringBuilder stringBuilder;

    public AutoResetAnsiString(Object... styleOrValues) {
        this.stringBuilder = new StringBuilder();
        for (Object valueOrStyle : styleOrValues) {
            append(valueOrStyle);
        }
        reset();
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    private void append(Object styleOrValue) {
        stringBuilder.append(styleOrValue.toString());
    }

    private void reset() {
        stringBuilder.append(FontStyle.NORMAL); // TODO use reset
    }
}
