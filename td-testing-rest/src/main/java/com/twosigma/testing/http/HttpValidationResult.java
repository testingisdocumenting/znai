package com.twosigma.testing.http;

import com.twosigma.testing.data.traceable.CheckLevel;
import com.twosigma.testing.http.datacoverage.DataNodeToMapOfValuesConverter;
import com.twosigma.testing.http.datacoverage.TraceableValueConverter;
import com.twosigma.testing.http.datanode.DataNode;
import com.twosigma.testing.reporter.TestStepPayload;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author mykola
 */
public class HttpValidationResult implements TestStepPayload {
    private String url;
    private String fullUrl;

    private HttpRequestBody requestBody;
    private HttpResponse response;
    private HeaderDataNode responseHeader;
    private DataNode responseBody;

    private String requestMethod;

    private List<String> mismatches;

    public HttpValidationResult(String requestMethod, String url, String fullUrl, 
                                HttpRequestBody requestBody,
                                HttpResponse response,
                                HeaderDataNode responseHeader, DataNode responseBody) {
        this.requestMethod = requestMethod;
        this.url = url;
        this.fullUrl = fullUrl;
        this.requestBody = requestBody;
        this.response = response;
        this.responseHeader = responseHeader;
        this.responseBody = responseBody;
        this.mismatches = new ArrayList<>();
    }

    public void addMismatch(String message) {
        mismatches.add(message);
    }

    public List<String> getMismatches() {
        return mismatches;
    }

    public String getUrl() {
        return url;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public HeaderDataNode getHeader() {
        return responseHeader;
    }

    public DataNode getBody() {
        return responseBody;
    }

    @Override
    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("method", requestMethod);
        result.put("url", fullUrl);

        if (requestBody != null) {
            result.put("requestType", requestBody.type());
            result.put("requestBody", requestBody.asString());
        }

        result.put("responseType", response.getContentType());
        result.put("responseBody", response.getContent());

        result.put("mismatches", getMismatches());

        Map<String, Object> responseBodyChecks = new LinkedHashMap<>();
        result.put("responseBodyChecks", responseBodyChecks);
        responseBodyChecks.put("failedPaths", extractPaths(responseBody, CheckLevel::isFailed));
        responseBodyChecks.put("passedPaths", extractPaths(responseBody, CheckLevel::isPassed));

        return result;
    }

    private List<String> extractPaths(DataNode dataNode, Function<CheckLevel, Boolean> includePath) {
        List<String> paths = new ArrayList<>();

        TraceableValueConverter traceableValueConverter = (id, traceableValue) -> {
            if (includePath.apply(traceableValue.getCheckLevel())) {
                paths.add(replaceStartOfThePath(id.getPath()));
            }

            return traceableValue.getValue();
        };

        DataNodeToMapOfValuesConverter dataNodeConverter = new DataNodeToMapOfValuesConverter(traceableValueConverter);
        dataNodeConverter.convert(dataNode);

        return paths;
    }

    private static String replaceStartOfThePath(String path) {
        if (path.startsWith("body")) {
            return path.replace("body", "root");
        }

        if (path.startsWith("header")) {
            return path.replace("header", "root");
        }

        throw new RuntimeException("path should start with either header or body");
    }
}
