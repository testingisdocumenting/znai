package com.twosigma.testing.http;

import java.util.ArrayList;
import java.util.List;

import com.twosigma.testing.http.datanode.DataNode;

/**
 * @author mykola
 */
public class HttpValidationResult {
    private String url;
    private String fullUrl;

    private HeaderDataNode header;
    private DataNode body;

    private String requestMethod;

    private List<String> mismatches;

    public HttpValidationResult(final String requestMethod, final String url, final String fullUrl, final HeaderDataNode header, final DataNode body) {
        this.requestMethod = requestMethod;
        this.url = url;
        this.fullUrl = fullUrl;
        this.header = header;
        this.body = body;
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
        return header;
    }

    public DataNode getBody() {
        return body;
    }
}
