package com.twosigma.documentation.openapi;

import com.twosigma.documentation.parser.commonmark.MarkdownParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class OpenApiSpec {
    private static final String DESCRIPTION_KEY = "description";
    private static final String REF_KEY = "$ref";
    private static final String ALL_OFF_KEY = "allOf";

    private MarkdownParser markdownParser;
    private Map<String, ?> spec;
    private List<OpenApiOperation> operations;

    /**
     * create open api spec representation from json.
     * Markdown defaultParser is required explicitly as open api defines description in common mark.
     *
     * @param markdownParser instance of markdown defaultParser
     * @param jsonSpec open api specification
     * @return open api spec
     */
    public static OpenApiSpec fromJson(MarkdownParser markdownParser, String jsonSpec) {
        return new OpenApiSpec(markdownParser, JsonUtils.deserializeAsMap(jsonSpec));
    }

    public OpenApiSpec(MarkdownParser markdownParser, Map<String, ?> spec) {
        this.markdownParser = markdownParser;
        this.spec = spec;
        this.operations = new ArrayList<>();

        parse();
    }

    public OpenApiOperation findOperationById(String operationId) {
        return operations.stream().filter(o -> operationId.equals(o.getId())).findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find operation: " + operationId));
    }

    public OpenApiOperation findOperationByMethodAndPath(String method, String path) {
        return operations.stream().filter(o -> o.matches(method, path)).findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find operation: " + method + " " + path));
    }

    public List<OpenApiOperation> findOperationsByTags(List<String> tags) {
        return operations.stream().filter(o -> o.hasTags(tags)).collect(toList());
    }

    public List<OpenApiOperation> getOperations() {
        return operations;
    }

    @SuppressWarnings("unchecked")
    private void parse() {
        parsePaths((Map<String, ?>) spec.get("paths"));
    }

    @SuppressWarnings("unchecked")
    private void parsePaths(Map<String, ?> paths) {
        if (paths == null) {
            throw new IllegalArgumentException("no paths definition found in the spec");
        }

        paths.forEach((path, methods) -> parseMethods(path, (Map<String, ?>) methods));
    }

    @SuppressWarnings("unchecked")
    private void parseMethods(String path, Map<String, ?> methods) {
        methods.forEach((method, definition) -> parseMethod(path, method, (Map<String, ?>) definition));
    }

    @SuppressWarnings("unchecked")
    private void parseMethod(String path, String method, Map<String, ?> definition) {
        OpenApiOperation operation = new OpenApiOperation();
        operation.setId(Objects.toString(definition.get("operationId")));
        operation.setTags((List<String>) definition.get("tags"));
        operation.setPath(path);
        operation.setMethod(method);
        operation.setSummary(Objects.toString(definition.get("summary")));
        operation.setDescription(parseMarkdown(definition.get("description")));
        operation.setResponses(buildResponses((Map<String, ?>) definition.get("responses")));
        operation.setParameters(buildParameters((List<Map<String, ?>>) definition.get("parameters")));

        operations.add(operation);
    }

    private List<OpenApiParameter> buildParameters(List<Map<String, ?>> parameters) {
        if (parameters == null) {
            return Collections.emptyList();
        }

        return parameters.stream().map(this::buildParameter).collect(toList());
    }

    @SuppressWarnings("unchecked")
    private OpenApiParameter buildParameter(Map<String, ?> parameter) {
        return new OpenApiParameter(
                Objects.toString(parameter.get("name")),
                Objects.toString(parameter.get("in")),
                Objects.toString(parameter.get("type")),
                getBoolean(parameter, "required", false),
                buildParameterSchema((Map<String, ?>) parameter.get("schema")),
                parseMarkdown(parameter.get("description")));
    }

    private boolean getBoolean(Map<String, ?> map, String name, Boolean defaultValue) {
        Object v = map.get(name);
        return v == null ? defaultValue : (boolean)v;
    }

    private Map<String, ?> buildParameterSchema(Map<String, ?> schema) {
        if (schema == null) {
            return Collections.emptyMap();
        }

        return substituteSchema(schema);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, ?>> buildResponses(Map<String, ?> specResponses) {
        List<Map<String, ?>> responses = new ArrayList<>();
        specResponses.forEach((code, response) -> responses.add(buildResponse(code, (Map<String, ?>) response)));

        return responses;
    }

    private Map<String, ?> buildResponse(String code, Map<String, ?> response) {
        Map<String, Object> normalized = new LinkedHashMap<>();
        normalized.put("code", code);
        normalized.putAll(substituteSchema(response));

        return normalized;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> substituteSchema(Map<String, ?> data) {
        Map<String, Object> substituted = new HashMap<>();
        data.forEach((k, v) -> {
            if (k.equals(DESCRIPTION_KEY) && v instanceof String) {
                substituted.put(DESCRIPTION_KEY, parseMarkdown(v));
                return;
            }

            switch (k) {
                case REF_KEY:
                    substituted.putAll((Map<? extends String, ?>) substituteValue(k, v));
                    break;
                case ALL_OFF_KEY:
                    substituted.putAll(combineAllOfProperties((List<Map<String, ?>>) v));
                    break;
                default:
                    substituted.put(k, substituteValue(k, v));
                    break;
            }
        });

        return substituted;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> combineAllOfProperties(List<Map<String, ?>> parts) {
        Map<String, Object> combined = new LinkedHashMap<>();
        Map<String, Object> properties = new LinkedHashMap<>();
        combined.put("properties", properties);

        parts.forEach(p -> {
            Map<String, ?> substituted = substituteSchema(p);
            properties.putAll((Map<? extends String, ?>) substituted.get("properties"));
        });

        return combined;
    }

    @SuppressWarnings("unchecked")
    private Object substituteValue(String k, Object v) {
        if (k.equals(REF_KEY)) {
            return schemaByPath(v.toString());
        }

        if (v instanceof Map) {
            return substituteSchema((Map<String, ?>) v);
        } else {
            return v;
        }
    }

    private Object schemaByPath(String path) {
        if (! path.startsWith("#")) {
            throw new IllegalArgumentException("definition path is not supported: " + path);
        }

        String[] pathParts = path.split("/");
        String[] pathFromRoot = Arrays.copyOfRange(pathParts, 1, pathParts.length);

        return specValueByPath(pathFromRoot);
    }

    @SuppressWarnings("unchecked")
    private Object specValueByPath(String[] pathParts) {
        Map<String, ?> data = spec;
        for (int i = 0; i < pathParts.length - 1; i++) {
            data = (Map<String, ?>) data.get(pathParts[i]);
        }

        return substituteSchema((Map<String, ?>) data.get(pathParts[pathParts.length - 1]));
    }

    private List<Map<String, Object>> parseMarkdown(Object markdown) {
        if (markdown == null) {
            return Collections.emptyList();
        }

        MarkupParserResult parserResult = markdownParser.parse(Paths.get(""), markdown.toString());
        return parserResult.getDocElement().getContent().stream().map(DocElement::toMap).collect(toList());
    }
}
