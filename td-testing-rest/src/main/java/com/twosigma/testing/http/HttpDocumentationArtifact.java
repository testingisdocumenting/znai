package com.twosigma.testing.http;

import com.twosigma.testing.http.datacoverage.DataNodeToMapOfValuesConverter;
import com.twosigma.testing.http.datacoverage.TraceableValueConverter;
import com.twosigma.utils.JsonUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.twosigma.testing.data.traceable.CheckLevel.None;

/**
 * @author mykola
 */
public class HttpDocumentationArtifact {
    private HttpValidationResult validationResult;

    public HttpDocumentationArtifact(HttpValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public String toJson() {
        return JsonUtils.serializePrettyPrint(toMap());
    }

    public Map<String, ?> toMap() {
        List<String> pathsToHighlight = new ArrayList<>();

        TraceableValueConverter traceableValueConverter = (id, traceableValue) -> {
            if (traceableValue.getCheckLevel() != None) {
                pathsToHighlight.add(id.getPath());
            }

            return traceableValue.getValue();
        };

        DataNodeToMapOfValuesConverter dataNodeConverter = new DataNodeToMapOfValuesConverter(traceableValueConverter);
        Object body = dataNodeConverter.convert(validationResult.getBody());
        String method = validationResult.getRequestMethod().toUpperCase();

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("method", method);
        result.put("url", validationResult.getUrl());
        result.put("body", body);
        result.put("paths", pathsToHighlight);

        return result;
    }
}
