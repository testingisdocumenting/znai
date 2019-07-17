package com.twosigma.znai.structure;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * custom information associated with a single page of documentation
 */
public class PageMeta {
    private Map<String, List<String>> meta;

    public PageMeta() {
        this.meta = Collections.emptyMap();
    }

    public PageMeta(Map<String, List<String>> meta) {
        this.meta = meta;
    }

    public boolean hasValue(String key) {
        return meta.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <E> E getSingleValue(String key) {
        List<String> multiValues = meta.get(key);
        if (multiValues != null) {
            return (E) multiValues.get(0);
        }

        return null;
    }

    public Map<String, ?> toMap() {
        return meta;
    }
}
