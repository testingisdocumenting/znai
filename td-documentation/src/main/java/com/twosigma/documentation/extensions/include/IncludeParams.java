package com.twosigma.documentation.extensions.include;

import com.twosigma.utils.JsonUtils;

import java.util.Collections;
import java.util.Map;

/**
 * @author mykola
 */
public class IncludeParams {
    private String freeParam;
    private Map<String, ?> opts;

    public IncludeParams(final String value) {
        this.freeParam = extractFreeParam(value);
        this.opts = extractOpts(value);
    }

    private String extractFreeParam(String value) {
        int optsStartIdx = value.indexOf('{');
        return (optsStartIdx != -1 ?
                value.substring(0, optsStartIdx):
                value).trim();
    }

    private Map<String, ?> extractOpts(String value) {
        int optsStartIdx = value.indexOf('{');
        if (optsStartIdx == -1) {
            return Collections.emptyMap();
        }

        String json = value.substring(optsStartIdx);
        return JsonUtils.deserializeAsMap(json);
    }

    public String getFreeParam() {
        return freeParam;
    }

    public Map<String, ?> getOpts() {
        return opts;
    }
}
