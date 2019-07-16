package com.twosigma.documentation.typescript;

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
