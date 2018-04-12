package com.twosigma.documentation.typescript;

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
