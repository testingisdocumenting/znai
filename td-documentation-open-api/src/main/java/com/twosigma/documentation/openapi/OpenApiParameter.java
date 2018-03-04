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
    private Map<String, ?> schema;
    private List<DocElement> description;

    public OpenApiParameter(String name, String in, String type, boolean required,
                            Map<String, ?> schema,
                            List<DocElement> description) {
        this.name = name;
        this.in = in;
        this.type = type;
        this.required = required;
        this.schema = schema;
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

    public Map<String, ?> getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "OpenApiParameter{" +
                "name='" + name + '\'' +
                ", in='" + in + '\'' +
                ", type='" + type + '\'' +
                ", required=" + required +
                ", schema=" + schema +
                ", description=" + description +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("in", in);
        result.put("type", type);
        result.put("required", required);
        result.put("schema", schema);
        result.put("description", description.stream().map(DocElement::toMap).collect(toList()));

        return result;
    }
}
