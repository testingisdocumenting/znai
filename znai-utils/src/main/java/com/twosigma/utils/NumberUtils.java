package com.twosigma.utils;

import java.text.NumberFormat;
import java.text.ParseException;

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

    public static boolean isInteger(String text) {
        if (text.isEmpty()) {
            return false;
        }

        for (int idx = 0; idx < text.length(); idx++) {
            if (!Character.isDigit(text.charAt(idx))) {
                return false;
            }
        }

        return true;
    }
}
