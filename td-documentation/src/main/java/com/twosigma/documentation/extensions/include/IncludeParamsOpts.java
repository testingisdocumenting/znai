package com.twosigma.documentation.extensions.include;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class IncludeParamsOpts {
    private Map<String, ?> opts;

    IncludeParamsOpts(Map<String, ?> opts) {
        this.opts = opts;
    }

    @SuppressWarnings("unchecked")
    public <E> E get(String name) {
        return (E) opts.get(name);
    }

    public String getRequiredString(String name) {
        Object v = opts.get(name);
        if (v == null) {
            throw new RuntimeException("'" + name + "' is required");
        }

        return v.toString();
    }

    public boolean has(String name) {
        return opts.containsKey(name);
    }

    public boolean isEmpty() {
        return opts.isEmpty();
    }

    public Map<String, ?> toMap() {
        return new LinkedHashMap<>(opts);
    }
}
