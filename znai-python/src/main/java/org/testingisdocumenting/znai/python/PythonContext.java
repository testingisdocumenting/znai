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

import java.util.HashSet;
import java.util.Set;

public class PythonContext {
    private final String fileName;
    private final String defaultPackageName;
    private final Set<String> knownTypes;

    public PythonContext(String fileName, String defaultPackageName) {
        this.fileName = fileName;
        this.defaultPackageName = defaultPackageName;
        this.knownTypes = new HashSet<>();
    }

    public String getFileName() {
        return fileName;
    }

    public String getDefaultPackageName() {
        return defaultPackageName;
    }

    public boolean isTypeDefined(String name) {
        return knownTypes.contains(name);
    }

    public void registerType(String name) {
        knownTypes.add(name);
    }
}
