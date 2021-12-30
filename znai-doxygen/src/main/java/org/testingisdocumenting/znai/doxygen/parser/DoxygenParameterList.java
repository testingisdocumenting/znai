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
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DoxygenParameterList {
    private final List<DoxygenParameter> list;

    public DoxygenParameterList() {
        list = new ArrayList<>();
    }

    public void add(DoxygenParameter parameter) {
        list.add(parameter);
    }

    public List<Map<String, ?>> toListOfMaps() {
        return list.stream()
                .map(DoxygenParameter::toMap)
                .collect(Collectors.toList());
    }

    public String generateCommaSeparatedTypes() {
        return list.stream()
                .map(p -> p.getType().buildCombinedText()
                        .replace(" &", "&")
                        .replace(" *", "*"))
                .collect(Collectors.joining(","));
    }

    public DoxygenParameter findByName(String name) {
        return list.stream().filter(p -> name.equals(p.getName()))
                .findFirst().orElse(null);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
