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

package org.testingisdocumenting.znai.python;

public class PythonParam {
    private final String name;
    private final String type;
    private final String pyDocText;

    public PythonParam(String name, String type, String pyDocText) {
        this.name = name;
        this.type = type;
        this.pyDocText = pyDocText;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPyDocText() {
        return pyDocText;
    }
}
