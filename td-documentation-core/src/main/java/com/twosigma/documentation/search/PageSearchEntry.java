package com.twosigma.documentation.search;

import com.twosigma.documentation.parser.PageSectionIdTitle;

/**
 * search entry local to a single page
 */
public class PageSearchEntry {
    private PageSectionIdTitle pageSectionIdTitle;
    private SearchText searchText;

    public PageSearchEntry(String pageSectionTitle, SearchText searchText) {
        this.pageSectionIdTitle = new PageSectionIdTitle(pageSectionTitle);
        this.searchText = searchText;
    }

    public String getPageSectionId() {
        return pageSectionIdTitle.getId();
    }

    public String getPageSectionTitle() {
        return pageSectionIdTitle.getTitle();
    }

    public SearchText getSearchText() {
        return searchText;
    }
}
