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

package org.testingisdocumenting.znai.parser.sphinx.python;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class PythonFunction {
    private String refId;
    private String name;
    private String description;
    private List<PythonFunctionParam> params;
    private List<PythonFunctionParamSignature> paramSignatures;

    public PythonFunction(String refId, String name, String description) {
        this.refId = refId;
        this.name = name;
        this.description = description;
        this.params = new ArrayList<>();
        this.paramSignatures = new ArrayList<>();
    }

    public void addParam(PythonFunctionParam param) {
        params.add(param);
    }

    public void addParamSignature(PythonFunctionParamSignature paramSignature) {
        paramSignatures.add(paramSignature);
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

    public List<PythonFunctionParam> getParams() {
        return params;
    }

    public List<PythonFunctionParamSignature> getParamSignatures() {
        return paramSignatures;
    }

    @Override
    public String toString() {
        return "PythonFunction{" +
                "refId='" + refId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", params=" + params +
                ", paramSignatures=" + paramSignatures +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("refId", refId);
        result.put("name", name);
        result.put("description", description);
        result.put("params", params.stream().map(PythonFunctionParam::toMap).collect(toList()));
        result.put("paramSignatures", paramSignatures.stream()
                .map(PythonFunctionParamSignature::toMap).collect(toList()));

        return result;
    }
}
