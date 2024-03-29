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

import org.testingisdocumenting.znai.extensions.api.ApiLinkedText;

import java.util.HashMap;
import java.util.Map;

public class DoxygenParameter {
    private final String name;
    private final ApiLinkedText type;

    public DoxygenParameter(String name, ApiLinkedText type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ApiLinkedText getType() {
        return type;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("type", type.toListOfMaps());

        return result;
    }
}
