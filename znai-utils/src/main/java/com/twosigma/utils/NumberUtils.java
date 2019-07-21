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
