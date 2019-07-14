package com.twosigma.documentation.parser.table;

import java.util.LinkedHashMap;
import java.util.Map;

public class Column {
    private String title;
    private String align;

    public Column(String title) {
        this.title = title.trim();
    }

    public Column(String title, String align) {
        this(title);
        this.align = align;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("title", title);
        if (align != null) {
            result.put("align", align);
        }

        return result;
    }
}
