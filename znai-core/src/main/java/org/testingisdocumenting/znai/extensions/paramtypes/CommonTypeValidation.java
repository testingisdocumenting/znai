/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.extensions.paramtypes;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

class CommonTypeValidation {
    static boolean matchString(Object value) {
        return value instanceof CharSequence;
    }

    static boolean matchStringOrNull(Object value) {
        return value == null || value instanceof CharSequence;
    }

    static boolean matchNumber(Object value) {
        return value instanceof Number;
    }

    static boolean matchStringOrNumber(Object value) {
        return matchString(value) || matchNumber(value);
    }

    static boolean matchBoolean(Object value) {
        return value instanceof Boolean;
    }

    static boolean matchObject(Object value) {
        return value instanceof Map;
    }

    static boolean matchListOrSingleString(Object value) {
        return matchListOrSingleValue(value, CommonTypeValidation::matchString);
    }

    static boolean matchListOrSingleNumber(Object value) {
        return matchListOrSingleValue(value, CommonTypeValidation::matchNumber);
    }

    static boolean matchListOrSingleStringWithNulls(Object value) {
        return matchListOrSingleValue(value, CommonTypeValidation::matchStringOrNull);
    }

    static boolean matchListOrSingleStringOrNumber(Object value) {
        return matchListOrSingleValue(value, CommonTypeValidation::matchStringOrNumber);
    }

    static boolean matchListOfAny(Object value) {
        return value instanceof List;
    }

    static boolean matchListOrSingleValue(Object value, Predicate<Object> predicate) {
        if (predicate.test(value)) {
            return true;
        }

        if (!(value instanceof List)) {
            return false;
        }

        List<?> listValue = (List<?>) value;
        return listValue.stream().allMatch(predicate);
    }
}
