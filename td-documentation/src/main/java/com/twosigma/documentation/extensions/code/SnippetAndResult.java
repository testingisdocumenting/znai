package com.twosigma.documentation.extensions.code;

/**
 * @author mykola
 */
public class SnippetAndResult {
    private String snippet;
    private String result;

    public SnippetAndResult(final String snippet, final String result) {
        this.snippet = snippet;
        this.result = result;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }
}
