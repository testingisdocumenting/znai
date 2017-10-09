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
    private Map<String, ?> docMetaMap;

    public DocMeta(Map<String, ?> docMetaMap) {
        this.type = docMetaMap.get("type").toString();
        this.title = docMetaMap.get("title").toString();
        this.docMetaMap = docMetaMap;
    }

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

    public String getTitle() {
        return title;
    }

    public void setLogo(final WebResource logo) {
        this.logo = logo;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("previewEnabled", previewEnabled);
        result.putAll(docMetaMap);

        return result;
    }

    public boolean isPreviewEnabled() {
        return previewEnabled;
    }

    public void setPreviewEnabled(boolean previewEnabled) {
        this.previewEnabled = previewEnabled;
    }
}
