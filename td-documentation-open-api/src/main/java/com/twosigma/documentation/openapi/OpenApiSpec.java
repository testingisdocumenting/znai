package com.twosigma.documentation.openapi;

import com.twosigma.documentation.parser.MarkdownParser;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Paths;
import java.util.*;

public class OpenApiSpec {
    private static final String REF_KEY = "$ref";

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
        operation.setDescription(buildDescription(definition));
        operation.setResponses(buildResponses((Map<String, ?>) definition.get("responses")));
        operation.setParameters(new ArrayList<>());

        operations.add(operation);
    }

    private List<DocElement> buildDescription(Map<String, ?> definition) {
        Object description = definition.get("description");

        if (description == null) {
            return Collections.emptyList();
        }

        MarkupParserResult parserResult = markdownParser.parse(Paths.get(""), description.toString());
        return parserResult.getDocElement().getContent();
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
            if (k.equals(REF_KEY)) {
                substituted.putAll((Map<? extends String, ?>) substituteValue(k, v));
            } else {
                substituted.put(k, substituteValue(k, v));
            }
        });

        return substituted;
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
}
