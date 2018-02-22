package com.twosigma.documentation.openapi;

import com.twosigma.documentation.parser.docelement.DocElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class OpenApiParameter {
    private String name;
    private String in;
    private String type;
    private boolean required;
    private List<DocElement> description;

    public OpenApiParameter(String name, String in, String type, boolean required, List<DocElement> description) {
        this.name = name;
        this.in = in;
        this.type = type;
        this.required = required;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getIn() {
        return in;
    }

    public String getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public List<DocElement> getDescription() {
        return description;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("in", in);
        result.put("type", type);
        result.put("required", required);
        result.put("description", description.stream().map(DocElement::toMap).collect(toList()));

        return result;
    }
}
