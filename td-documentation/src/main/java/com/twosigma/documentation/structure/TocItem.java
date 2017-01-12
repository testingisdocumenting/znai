package com.twosigma.documentation.structure;

import com.twosigma.documentation.utils.NameUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class TocItem {
    private String dirName;
    private String fileNameWithoutExtension;
    private String sectionTitle;
    private String pageTitle;

    private TocItem next;
    private TocItem prev;

    public TocItem(final String dirName, final String fileNameWithoutExtension) {
        this.dirName = dirName;
        this.fileNameWithoutExtension = fileNameWithoutExtension;

        this.sectionTitle = NameUtils.dashToCamelCaseWithSpaces(dirName);
        this.pageTitle = NameUtils.dashToCamelCaseWithSpaces(fileNameWithoutExtension);
    }

    public TocItem getNext() {
        return next;
    }

    void setNext(final TocItem next) {
        this.next = next;
    }

    public TocItem getPrev() {
        return prev;
    }

    void setPrev(final TocItem prev) {
        this.prev = prev;
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

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sectionTitle", getSectionTitle());
        result.put("pageTitle", getPageTitle());
        result.put("fileName", getFileNameWithoutExtension());
        result.put("dirName", getDirName());

        return result;
    }

    @Override
    public String toString() {
        return "(dirName:" + dirName + ", fileNameWithoutExtension: " + fileNameWithoutExtension + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TocItem tocItem = (TocItem) o;

        if (!dirName.equals(tocItem.dirName)) {
            return false;
        }
        return fileNameWithoutExtension.equals(tocItem.fileNameWithoutExtension);

    }

    @Override
    public int hashCode() {
        int result = dirName.hashCode();
        result = 31 * result + fileNameWithoutExtension.hashCode();
        return result;
    }
}
