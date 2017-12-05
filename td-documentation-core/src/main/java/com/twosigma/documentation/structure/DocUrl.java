package com.twosigma.documentation.structure;

/**
 * @author mykola
 */
public class DocUrl {
    private static final String LINK_TO_SECTION_INSTRUCTION = "To refer to a section of this document use" +
            " dir-name/file-name[#page-section-id]";

    private String dirName = "";
    private String fileName = "";
    private String pageSectionId = "";

    private String url;

    private boolean isGlobalUrl;
    private boolean isIndexUrl;

    public static DocUrl indexUrl() {
        return new DocUrl(true);
    }

    private DocUrl(boolean isIndexUrl) {
        this.isIndexUrl = isIndexUrl;
    }

    public DocUrl(String dirName, String fileName, String pageSectionId) {
        this.dirName = dirName;
        this.fileName = fileName;
        this.pageSectionId = pageSectionId;
    }

    public DocUrl(String url) {
        this.url = url;
        if (url.startsWith("http") || url.startsWith("file") || url.startsWith("mailto")) {
            isGlobalUrl = true;
            return;
        }

        if (url.equals("/")) {
            isIndexUrl = true;
            return;
        }

        if (url.startsWith("..")) {
            throw new IllegalArgumentException("Do not use .. based urls: " + url + ". " + LINK_TO_SECTION_INSTRUCTION);
        }

        String[] parts = url.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Unexpected url pattern: " + url + ". " + LINK_TO_SECTION_INSTRUCTION);
        }

        dirName = parts[0];

        int idxOfPageSectionSep = parts[1].indexOf('#');

        fileName = idxOfPageSectionSep == -1 ? parts[1] : parts[1].substring(0, idxOfPageSectionSep);
        pageSectionId = idxOfPageSectionSep == -1 ? "" : parts[1].substring(idxOfPageSectionSep + 1);
    }

    public boolean isIndexUrl() {
        return isIndexUrl;
    }

    public boolean isGlobalUrl() {
        return isGlobalUrl;
    }

    public String getDirName() {
        return dirName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPageSectionId() {
        return pageSectionId;
    }

    public String getUrl() {
        return url;
    }
}
