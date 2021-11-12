/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.doxygen.parser;

public class DoxygenUtils {
    private DoxygenUtils() {
    }

    public static String fullName(String compoundKind, String compoundName, String name) {
        if (compoundNameOrEmptyForFile(compoundKind, compoundName).isEmpty()) {
            return name;
        }

        // TODO separator for Java/Python/etc
        return compoundName + "::" + name;
    }

    public static String compoundNameOrEmptyForFile(String compoundKind, String compoundName) {
        if ("file".equals(compoundKind)) {
            return "";
        }

        return compoundName;
    }
}
