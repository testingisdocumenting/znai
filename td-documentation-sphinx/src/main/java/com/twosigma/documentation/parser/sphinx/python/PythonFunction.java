package com.twosigma.documentation.parser.sphinx.python;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class PythonFunction {
    private String name;
    private String description;
    private List<PythonFunctionParam> params;
    private List<PythonFunctionParamSignature> paramSignatures;

    public PythonFunction(String name, String description) {
        this.name = name;
        this.description = description;
        this.params = new ArrayList<>();
        this.paramSignatures = new ArrayList<>();
    }

    public void addParam(PythonFunctionParam param) {
        params.add(param);
    }

    public void addParamSignature(PythonFunctionParamSignature paramSignature) {
        paramSignatures.add(paramSignature);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PythonFunctionParam> getParams() {
        return params;
    }

    public List<PythonFunctionParamSignature> getParamSignatures() {
        return paramSignatures;
    }

    @Override
    public String toString() {
        return "PythonFunction{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", params=" + params +
                ", paramSignatures=" + paramSignatures +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("params", params.stream().map(PythonFunctionParam::toMap).collect(toList()));
        result.put("paramSignatures", paramSignatures.stream()
                .map(PythonFunctionParamSignature::toMap).collect(toList()));

        return result;
    }
}
