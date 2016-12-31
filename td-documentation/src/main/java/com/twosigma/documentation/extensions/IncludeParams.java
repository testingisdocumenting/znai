package com.twosigma.documentation.extensions;

import java.util.Map;

/**
 * @author mykola
 */
public class IncludeParams {
    private String freeParam;
    private Map<String, String> opts;

    public IncludeParams(final String freeParam, final Map<String, String> opts) {
        this.freeParam = freeParam;
        this.opts = opts;
    }

    public String getFreeParam() {
        return freeParam;
    }

    public Map<String, String> getOpts() {
        return opts;
    }
}
