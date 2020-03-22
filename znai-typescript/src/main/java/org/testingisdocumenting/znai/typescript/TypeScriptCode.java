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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeScriptCode {
    private List<TypeScriptType> types;
    private List<TypeScriptFunction> functions;

    public TypeScriptCode(List<Map<String, ?>> entries) {
        types = entries.stream()
                .filter(e -> "type".equals(e.get("kind")))
                .map(TypeScriptType::new).collect(Collectors.toList());

        functions = entries.stream()
                .filter(e -> "function".equals(e.get("kind")))
                .map(TypeScriptFunction::new).collect(Collectors.toList());
    }

    public TypeScriptFunction findFunction(String name) {
        return functions.stream().filter(t -> t.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find function with '" + name + "' name"));
    }

    public TypeScriptType findType(String name) {
        return types.stream().filter(t -> t.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find type with '" + name + "' name"));
    }
}
