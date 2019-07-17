package com.twosigma.znai.search;

import com.twosigma.znai.parser.PageSectionIdTitle;

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
