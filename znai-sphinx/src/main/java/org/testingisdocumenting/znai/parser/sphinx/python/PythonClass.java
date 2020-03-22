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

package com.twosigma.znai.parser.sphinx.python;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class PythonClass {
    private String refId;
    private String name;
    private String description;
    private List<PythonFunction> methods;

    public PythonClass(String refId, String name, String description) {
        this.refId = refId;
        this.name = name;
        this.description = description;
        methods = new ArrayList<>();
    }

    public void addMethod(PythonFunction method) {
        methods.add(method);
    }

    public String getRefId() {
        return refId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PythonFunction> getMethods() {
        return methods;
    }

    @Override
    public String toString() {
        return "PythonClass{" +
                "refId='" + refId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", methods=" + methods +
                '}';
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("refId", refId);
        result.put("name", name);
        result.put("description", description);
        result.put("methods", methods.stream().map(PythonFunction::toMap).collect(toList()));

        return result;
    }
}
