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

package org.testingisdocumenting.znai.python;

import java.util.Arrays;
import java.util.stream.Collectors;

class PythonUtils {
    static String convertQualifiedNameToFilePath(String qualifiedName) {
        String[] parts = qualifiedName.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("expect the qualified name to be of a form: module.[optional.].name, given: " + qualifiedName);
        }

        return Arrays.stream(parts)
                .limit(parts.length - 1)
                .collect(Collectors.joining("/")) + ".py";
    }

    static String entityNameFromQualifiedName(String qualifiedName) {
        String[] parts = qualifiedName.split("\\.");
        return parts[parts.length - 1];
    }
}
