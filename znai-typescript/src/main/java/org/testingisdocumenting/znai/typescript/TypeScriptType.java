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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeScriptType {
    private String name;
    private List<TypeScriptProperty> properties;

    @SuppressWarnings("unchecked")
    public TypeScriptType(Map<String, ?> entry) {
        name = entry.get("name").toString();

        List<Map<String, ?>> members = (List<Map<String, ?>>) entry.get("members");
        properties = members.stream()
                .filter(m -> "property".equals(m.get("kind")))
                .map(TypeScriptProperty::new)
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public List<TypeScriptProperty> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "TypeScriptType{" +
                "name='" + name + '\'' +
                ", properties=" + properties +
                '}';
    }
}
