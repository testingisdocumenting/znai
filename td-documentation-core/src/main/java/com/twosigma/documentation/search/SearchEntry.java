package com.twosigma.documentation.search;

/**
 * @author mykola
 */
public class SearchEntry {
    private String docTitle;
    private String sectionTitle;
    private String pageTitle;
    private String pageSectionTitle;
    private SearchText text;

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageSectionTitle() {
        return pageSectionTitle;
    }

    public void setPageSectionTitle(String pageSectionTitle) {
        this.pageSectionTitle = pageSectionTitle;
    }

    public SearchText getText() {
        return text;
    }

    public void setText(SearchText text) {
        this.text = text;
    }
}
