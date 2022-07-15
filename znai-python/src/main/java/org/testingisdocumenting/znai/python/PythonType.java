/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.python;

import org.testingisdocumenting.znai.extensions.api.ApiLinkedText;
import org.testingisdocumenting.znai.structure.DocStructure;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PythonType {
    private final String name;

    /**
     * in case of a container type like Union[type1, type2] or dict[string, int]
     */
    private final List<PythonType> types;
    private final PythonContext context;

    public PythonType(Object parsed, PythonContext context) {
        this.context = context;

        if (parsed instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) parsed;

            this.name = nameWithAppliedDefault(map.get("name").toString());
            this.types = extractTypes(map);
        } else if (parsed != null) {
            this.name = nameWithAppliedDefault(parsed.toString());
            this.types = Collections.emptyList();
        } else {
            this.name = "";
            this.types = Collections.emptyList();
        }
    }

    public boolean isDefined() {
        return !name.isEmpty();
    }

    public String getName() {
        return name;
    }

    public List<PythonType> getTypes() {
        return types;
    }

    public String renderTypeAsString() {
        StringBuilder result = new StringBuilder();
        result.append(name);
        if (!types.isEmpty()) {
            result.append("[");
            result.append(types.stream().map(PythonType::renderTypeAsString).collect(Collectors.joining(", ")));
            result.append("]");
        }

        return result.toString();
    }

    public ApiLinkedText convertToApiLinkedText(DocStructure docStructure) {
        ApiLinkedText linkedText = new ApiLinkedText();

        if (name.isEmpty()) {
            return linkedText;
        }

        Supplier<String> typeUrlSupplier = () -> docStructure.findGlobalAnchorUrl(PythonUtils.globalAnchorId(name)).orElse("");
        linkedText.addPart(name, typeUrlSupplier);

        if (!types.isEmpty()) {
            linkedText.addPart("[");

            int idx = 0;
            for (PythonType type : types) {
                ApiLinkedText nested = type.convertToApiLinkedText(docStructure);
                linkedText.addParts(nested);
                if (idx < types.size() - 1) {
                    linkedText.addPart(", ");
                }

                idx++;
            }

            linkedText.addPart("]");
        }

        return linkedText;
    }

    @Override
    public String toString() {
        return "PythonCodeType{" +
                "name='" + name + '\'' +
                ", types=" + types +
                '}';
    }

    private String nameWithAppliedDefault(String name) {
        if (name.isEmpty()) {
            return name;
        }

        if (context.getDefaultPackageName().isEmpty()) {
            return name;
        }

        // for classes defined in the same file, we add default package name,
        // so they have fully qualified name
        if (!name.contains(".") && context.isTypeDefined(name)) {
            return context.getDefaultPackageName() + "." + name;
        }

        return name;
    }

    @SuppressWarnings("unchecked")
    private List<PythonType> extractTypes(Map<String, Object> map) {
        List<Object> list = (List<Object>) map.get("types");
        return list.stream().map(parsed -> new PythonType(parsed, context)).collect(Collectors.toList());
    }
}
