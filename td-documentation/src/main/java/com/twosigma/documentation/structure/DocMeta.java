package com.twosigma.documentation.structure;

import java.util.HashMap;
import java.util.Map;

import com.twosigma.documentation.html.WebResource;

/**
 * @author mykola
 */
public class DocMeta {
    private String type;
    private String title;
    private WebResource logo;
    private boolean previewEnabled;

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public WebResource getLogo() {
        return logo;
    }

    public void setLogo(final WebResource logo) {
        this.logo = logo;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("title", title);
        result.put("previewEnabled", previewEnabled);
        result.put("logo", logo.getPath());

        return result;
    }

    public boolean isPreviewEnabled() {
        return previewEnabled;
    }

    public void setPreviewEnabled(boolean previewEnabled) {
        this.previewEnabled = previewEnabled;
    }
}
