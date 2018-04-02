package com.twosigma.documentation.typescript;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeScriptCode {
    private List<TypeScriptType> types;

    public TypeScriptCode(List<Map<String, ?>> entries) {
        types = entries.stream().map(TypeScriptType::new).collect(Collectors.toList());
    }

    public TypeScriptType findType(String name) {
        return types.stream().filter(t -> t.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find type with '" + name + "' name"));
    }
}
