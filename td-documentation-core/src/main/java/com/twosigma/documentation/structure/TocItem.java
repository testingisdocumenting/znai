package com.twosigma.documentation.structure;

import com.twosigma.documentation.parser.PageSectionIdTitle;
import com.twosigma.documentation.utils.NameUtils;

import java.util.*;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class TocItem {
    private static final Pattern fileNameAllowedPattern = Pattern.compile("[a-zA-Z0-9-_]*");

    public static final String INDEX = "index";

    private String dirName;
    private String fileNameWithoutExtension;
    private String sectionTitle;
    private String pageTitle;
    private PageMeta pageMeta;

    private List<PageSectionIdTitle> pageSectionIdTitles;

    static TocItem createIndex() {
        return new TocItem("", INDEX);
    }

    public TocItem(String dirName, String fileNameWithoutExtension) {
        this.dirName = dirName;
        this.fileNameWithoutExtension = fileNameWithoutExtension;
        validateFileName(this.dirName);
        validateFileName(this.fileNameWithoutExtension);

        this.sectionTitle = NameUtils.dashToCamelCaseWithSpaces(dirName);
        this.pageTitle = NameUtils.dashToCamelCaseWithSpaces(fileNameWithoutExtension);

        this.pageMeta = new PageMeta(Collections.emptyMap());

        this.pageSectionIdTitles = new ArrayList<>();
    }

    public TocItem(String dirName, String fileNameWithoutExtension, String sectionTitle) {
        this(dirName, fileNameWithoutExtension);
        this.sectionTitle = sectionTitle;
    }

    public String getDirName() {
        return dirName;
    }

    public String getFileNameWithoutExtension() {
        return fileNameWithoutExtension;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageMeta(PageMeta pageMeta) {
        this.pageMeta = pageMeta;
    }

    public List<PageSectionIdTitle> getPageSectionIdTitles() {
        return pageSectionIdTitles;
    }

    public boolean hasPageSection(String id) {
        return pageSectionIdTitles.stream().anyMatch(ps -> ps.getId().equals(id));
    }

    public void setPageSectionIdTitles(List<PageSectionIdTitle> pageSectionIdTitles) {
        this.pageSectionIdTitles = pageSectionIdTitles;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public boolean isIndex() {
        return dirName.isEmpty() && fileNameWithoutExtension.equals(INDEX);
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sectionTitle", getSectionTitle());
        result.put("pageTitle", getPageTitle());
        result.put("pageMeta", pageMeta.toMap());
        result.put("fileName", getFileNameWithoutExtension());
        result.put("dirName", getDirName());
        result.put("pageSectionIdTitles",
                getPageSectionIdTitles().stream().map(PageSectionIdTitle::toMap).collect(toList()));

        return result;
    }

    @Override
    public String toString() {
        return dirName + "/" + fileNameWithoutExtension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TocItem tocItem = (TocItem) o;

        return dirName.equals(tocItem.dirName) &&
                fileNameWithoutExtension.equals(tocItem.fileNameWithoutExtension);
    }

    @Override
    public int hashCode() {
        int result = dirName.hashCode();
        result = 31 * result + fileNameWithoutExtension.hashCode();
        return result;
    }

    private void validateFileName(String name) {
        if (! fileNameAllowedPattern.matcher(name).matches()) {
            throw new IllegalArgumentException("file name should match: " + fileNameAllowedPattern + "\ngiven name: " +
                    name + "\n" +
                    "use\n---\ntitle: my custom title with any symbols like !#?\n---\nto override the title for your page");
        }
    }
}
