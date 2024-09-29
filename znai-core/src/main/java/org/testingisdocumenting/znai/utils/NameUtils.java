/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NameUtils {
    public static String idFromTitle(final String title) {
        if (title == null)
            return null;

        String onlyTextAndNumbers = title.replaceAll("[^a-zA-Z0-9-_/ ]", "");
        return onlyTextAndNumbers.toLowerCase().replaceAll("[\\s./]+", "-");
    }

    public static String dashToCamelCaseWithSpaces(final String dashBasedName) {
        if (dashBasedName == null)
            return null;

        final String[] parts = dashBasedName.split("-");
        return Arrays.stream(parts).
            filter(p -> ! p.isEmpty()).
            map(p -> Character.toUpperCase(p.charAt(0)) + p.substring(1)).
            collect(Collectors.joining(" "));
    }
}
