package com.twosigma.documentation.structure;

import java.util.HashMap;
import java.util.Map;

import com.twosigma.documentation.html.WebResource;

/**
 * @author mykola
 */
public class DocMeta {
    private String id;
    private String type;
    private String title;
    private WebResource logo;
    private boolean previewEnabled;

    /**
     * Documentation can be standalone and hosted by itself on a server. Or you may have multiple documentations
     * that are hosted within an organization. In later case it is important to have an id.
     *
     * @return documentation id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        result.put("id", id);
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
