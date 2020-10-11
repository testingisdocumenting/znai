/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.extensions.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ApiParameter {
    private final String anchorId;
    private final String name;
    private final String type;
    private final List<Map<String, Object>> description;
    private final String textForSearch;

    private final List<ApiParameter> children;

    public ApiParameter(String anchorId, String name, String type,
                        List<Map<String, Object>> description, String textForSearch) {
        this.anchorId = anchorId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.textForSearch = textForSearch;
        this.children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<Map<String, Object>> getDescription() {
        return description;
    }

    public String getTextForSearch() {
        return textForSearch;
    }

    public String combinedTextForSearch() {
        List<String> parts = new ArrayList<>();
        parts.add(name);
        parts.add(type);
        parts.add(textForSearch);

        children.forEach(c -> parts.add(c.combinedTextForSearch()));

        return String.join(" ", parts);
    }

    public List<String> collectAllAnchors() {
        List<String> result = new ArrayList<>();
        result.add(anchorId);

        children.forEach(child -> result.addAll(child.collectAllAnchors()));
        return result;
    }

    public List<ApiParameter> getChildren() {
        return children;
    }

    public ApiParameter add(String name, String type, List<Map<String, Object>> description, String textForSearch) {
        ApiParameter apiParameter = new ApiParameter(
                ApiParametersAnchors.anchorIdFromNameAndPrefix(anchorId, name),
                name, type, description, textForSearch);
        children.add(apiParameter);

        return apiParameter;
    }

    ApiParameter find(String name) {
        return children.stream()
                .filter(p -> p.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no children found with name: '" + name +
                        "' in '" + this.name + "'"));
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("type", type);
        result.put("anchorId", anchorId);
        result.put("description", description);
        if (! children.isEmpty()) {
            result.put("children", children.stream().map(ApiParameter::toMap).collect(toList()));
        }

        return result;
    }
}
