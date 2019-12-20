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

package com.twosigma.znai.extensions.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ApiParameter {
    private final String name;
    private final String type;
    private final List<Map<String, Object>> description;

    private final List<ApiParameter> children;

    public ApiParameter(String name, String type, List<Map<String, Object>> description) {
        this.name = name;
        this.type = type;
        this.description = description;
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

    public List<ApiParameter> getChildren() {
        return children;
    }

    public ApiParameter add(String name, String type, List<Map<String, Object>> description) {
        ApiParameter apiParameter = new ApiParameter(name, type, description);
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
        result.put("description", description);
        if (! children.isEmpty()) {
            result.put("children", children.stream().map(ApiParameter::toMap).collect(toList()));
        }

        return result;
    }
}
