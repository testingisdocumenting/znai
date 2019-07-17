package com.twosigma.znai.parser;

import com.twosigma.znai.utils.NameUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class PageSectionIdTitle {
    private String title;
    private String id;

    public PageSectionIdTitle(String title) {
        this.title = title;
        this.id = NameUtils.idFromTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("title", getTitle());
        result.put("id", getId());

        return result;
    }
}
