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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DoxygenMember {
    protected String compoundName;
    protected String name;
    protected DoxygenTextWithLinks returnType;
    protected List<DoxygenParameter> parameters;
    protected DoxygenDescription description;

    public DoxygenMember() {
        parameters = new ArrayList<>();
    }

    public DoxygenDescription getDescription() {
        return description;
    }

    public void addParameter(String name, DoxygenTextWithLinks type) {
        parameters.add(new DoxygenParameter(name, type));
    }

    public String getCompoundName() {
        return compoundName;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("compoundName", compoundName);
        result.put("name", name);
        result.put("returnType", returnType.toListOfMaps());
        result.put("parameters", parameters.stream().map(DoxygenParameter::toMap).collect(Collectors.toList()));

        return result;
    }
}
