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

public class PythonProperty {
    private final String name;
    private final PythonType type;
    private final boolean isReadOnly;
    private final String pyDocText;

    public PythonProperty(String name, PythonType type, boolean isReadOnly, String pyDocText) {
        this.name = name;
        this.type = type;
        this.isReadOnly = isReadOnly;
        this.pyDocText = pyDocText;
    }

    public String getName() {
        return name;
    }

    public PythonType getType() {
        return type;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public String getPyDocText() {
        return pyDocText;
    }
}
