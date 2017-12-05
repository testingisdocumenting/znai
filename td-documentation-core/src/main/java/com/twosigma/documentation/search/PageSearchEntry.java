package com.twosigma.documentation.search;

import com.twosigma.documentation.parser.PageSectionIdTitle;

/**
 * search entry local to a single page
 * @author mykola
 */
public class PageSearchEntry {
    private PageSectionIdTitle pageSectionIdTitle;
    private SearchText text;

    public PageSearchEntry(String pageSectionTitle, SearchText text) {
        this.pageSectionIdTitle = new PageSectionIdTitle(pageSectionTitle);
        this.text = text;
    }

    public String getPageSectionId() {
        return pageSectionIdTitle.getId();
    }

    public String getPageSectionTitle() {
        return pageSectionIdTitle.getTitle();
    }

    public SearchText getText() {
        return text;
    }
}
