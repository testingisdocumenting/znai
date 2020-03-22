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

package org.testingisdocumenting.znai.typescript;

import java.util.LinkedHashMap;
import java.util.Map;

public class TypeScriptJsxAttr {
    private String name;
    private String value;

    @SuppressWarnings("unchecked")
    public TypeScriptJsxAttr(Map<String, ?> entry) {
        this.name = entry.get("name").toString();
        this.value = entry.get("value").toString();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("value", value);

        return result;
    }
}
