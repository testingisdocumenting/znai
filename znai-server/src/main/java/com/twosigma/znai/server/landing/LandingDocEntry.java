package com.twosigma.znai.server.landing;

import java.util.LinkedHashMap;
import java.util.Map;

public class LandingDocEntry {
    private String id;
    private String name;
    private String url;
    private String category;
    private String description;

    public LandingDocEntry(String id, String name, String url, String category, String description) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.category = category;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("url", url);
        result.put("category", category);
        result.put("description", description);

        return result;
    }
}
