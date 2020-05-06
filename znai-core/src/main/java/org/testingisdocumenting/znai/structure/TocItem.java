/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.structure;

import org.testingisdocumenting.znai.parser.PageSectionIdTitle;
import org.testingisdocumenting.znai.utils.NameUtils;

import java.util.*;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class TocItem {
    private static final Pattern fileNameAllowedPattern = Pattern.compile("[a-zA-Z0-9-_]*");

    public static final String INDEX = "index";

    private String dirName;
    private String fileNameWithoutExtension;
    private String sectionTitle;
    private String pageTitle;
    private PageMeta pageMeta;

    /**
     *  relative location on github or other source.
     *  Note it is not necessarily dirName + fileNameWithoutExtension + extension,
     *  i.e. original source can be deployed into a different location based on a {@link org.testingisdocumenting.znai.parser.MarkupParsingConfiguration}
     */
    private String viewOnRelativePath;

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

    public void setViewOnRelativePath(String viewOnRelativePath) {
        this.viewOnRelativePath = viewOnRelativePath;
    }

    public String getViewOnRelativePath() {
        return viewOnRelativePath;
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

    public boolean match(String dirName, String fileNameWithoutExtension) {
        return getDirName().equals(dirName) && getFileNameWithoutExtension().equals(fileNameWithoutExtension);
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sectionTitle", getSectionTitle());
        result.put("pageTitle", getPageTitle());
        result.put("pageMeta", pageMeta.toMap());
        result.put("dirName", getDirName());
        result.put("fileName", getFileNameWithoutExtension());
        result.put("viewOnRelativePath", viewOnRelativePath);
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
