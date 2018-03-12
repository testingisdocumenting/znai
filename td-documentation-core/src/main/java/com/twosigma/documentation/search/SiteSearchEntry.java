package com.twosigma.documentation.search;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * global search entry
 * @author mykola
 */
public class SiteSearchEntry {
    private String url;
    private String fullTitle;
    private SearchText text;

    public SiteSearchEntry() {
    }

    public SiteSearchEntry(String url, String fullTitle, SearchText text) {
        this.url = url;
        this.fullTitle = fullTitle;
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public SearchText getText() {
        return text;
    }

    public void setText(SearchText text) {
        this.text = text;
    }
}
