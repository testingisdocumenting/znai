package com.twosigma.utils;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author mykola
 */
public class NumberUtils {
    private NumberUtils() {
    }

    public static Number convertStringToNumber(String text) {
        try {
            return NumberFormat.getInstance().parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
