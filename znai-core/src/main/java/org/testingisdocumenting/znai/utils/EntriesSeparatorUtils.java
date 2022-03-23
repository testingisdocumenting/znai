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

package org.testingisdocumenting.znai.utils;

public class EntriesSeparatorUtils {
    private EntriesSeparatorUtils() {
    }

    /**
     * <ul>
     *     <li>null separator becomes double new-line separator (empty line in between)</li>
     *     <li>empty separator becomes single new-line separator</li>
     *     <li>text separator becomes single new-line separators surrounding provided separator</li>
     * </ul>
     *
     * @param separator user provided separator
     * @return enriched separator
     */
    public static String enrichUserTextEntriesSeparator(String separator) {
        if (separator == null) {
            return "\n";
        } else if (separator.isEmpty()) {
            return "\n";
        } else if (separator.equals("\n")) {
            return "\n\n";
        } else {
            return "\n" + separator + "\n";
        }
    }
}
