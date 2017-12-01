package com.twosigma.documentation.search;

/**
 * search entry local to a single page
 * @author mykola
 */
public class PageSearchEntry {
    private String pageSectionTitle;
    private SearchText text;

    public PageSearchEntry(String pageSectionTitle, SearchText text) {
        this.pageSectionTitle = pageSectionTitle;
        this.text = text;
    }

    public String getPageSectionTitle() {
        return pageSectionTitle;
    }

    public SearchText getText() {
        return text;
    }
}
