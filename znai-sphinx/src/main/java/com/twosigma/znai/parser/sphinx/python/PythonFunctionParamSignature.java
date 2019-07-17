package com.twosigma.znai.parser.sphinx.python;

import java.util.LinkedHashMap;
import java.util.Map;

public class PythonFunctionParamSignature {
    private String givenExample;
    private boolean isOptional;

    public PythonFunctionParamSignature(String givenExample, boolean isOptional) {
        this.givenExample = givenExample;
        this.isOptional = isOptional;
    }

    public String getGivenExample() {
        return givenExample;
    }

    public boolean isOptional() {
        return isOptional;
    }

    @Override
    public String toString() {
        return "PythonFunctionParamSignature{" +
                "givenExample='" + givenExample + '\'' +
                ", isOptional=" + isOptional +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("givenExample", givenExample);
        result.put("isOptional", isOptional);

        return result;
    }
}
