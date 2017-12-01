package com.twosigma.documentation.search;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * global search entry
 * @author mykola
 */
@XmlRootElement
public class SiteSearchEntry {
    private String fullTitle;
    private SearchText text;

    public SiteSearchEntry() {
    }

    public SiteSearchEntry(String fullTitle, SearchText text) {
        this.fullTitle = fullTitle;
        this.text = text;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public void setText(SearchText text) {
        this.text = text;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public SearchText getText() {
        return text;
    }
}
