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

package com.twosigma.znai.typescript;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TypeScriptJsxDeclaration {
    private String tagName;
    private List<TypeScriptJsxAttr> attributes;

    @SuppressWarnings("unchecked")
    public TypeScriptJsxDeclaration(Map<String, ?> entry) {
        this.tagName = entry.get("tagName").toString();

        List<Map<String, ?>> attrs = (List<Map<String, ?>>) entry.get("attributes");
        this.attributes = attrs.stream()
                .map(TypeScriptJsxAttr::new)
                .collect(Collectors.toList());
    }

    public String getTagName() {
        return tagName;
    }

    public List<TypeScriptJsxAttr> getAttributes() {
        return attributes;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tagName", tagName);
        result.put("attributes", attributes.stream().map(TypeScriptJsxAttr::toMap).collect(toList()));

        return result;
    }
}
