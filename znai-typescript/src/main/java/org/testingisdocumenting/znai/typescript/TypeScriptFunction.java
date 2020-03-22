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

public class TypeScriptFunction {
    private String name;
    private List<TypeScriptJsxDeclaration> jsxDeclarations;

    @SuppressWarnings("unchecked")
    public TypeScriptFunction(Map<String, ?> entry) {
        name = entry.get("name").toString();

        List<Map<String, ?>> declarations = (List<Map<String, ?>>) entry.get("jsxDeclarations");
        jsxDeclarations = declarations.stream()
                .map(TypeScriptJsxDeclaration::new)
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public List<TypeScriptJsxDeclaration> getJsxDeclarations() {
        return jsxDeclarations;
    }
}
