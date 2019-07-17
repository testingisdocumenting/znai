package com.twosigma.znai.search;

import java.util.Objects;

/**
 * global search entry
 */
public class GlobalSearchEntry {
    private String url;
    private String fullTitle;
    private SearchText text;

    public GlobalSearchEntry() {
    }

    public GlobalSearchEntry(String url, String fullTitle, SearchText text) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GlobalSearchEntry that = (GlobalSearchEntry) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(fullTitle, that.fullTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, fullTitle);
    }
}
