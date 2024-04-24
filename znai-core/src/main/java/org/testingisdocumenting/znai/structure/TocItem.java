/*
 * Copyright 2020 znai maintainers
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

import java.util.*;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class TocItem {
    private static final Pattern fileNameAllowedPattern = Pattern.compile("[a-zA-Z\\d-_]*");

    public static final String INDEX = "index";

    private final TocNameAndOpts chapter;
    private final TocNameAndOpts page;
    private final String fileNameWithoutExtension;
    private final String fileExtension;
    private String pageTitle;
    private PageMeta pageMeta;

    /**
     *  relative location on GitHub or other source.
     *  Note it is not necessarily dirName + fileNameWithoutExtension + extension,
     *  i.e. original source can be deployed into a different location based on a {@link org.testingisdocumenting.znai.parser.MarkupParsingConfiguration}
     */
    private String viewOnRelativePath;

    private List<PageSectionIdTitle> pageSectionIdTitles;

    static TocItem createIndex() {
        return new TocItem(new TocNameAndOpts(""), new TocNameAndOpts(INDEX), "");
    }

    public TocItem(TocNameAndOpts chapter, TocNameAndOpts page, String defaultExtension) {
        this.chapter = chapter;
        this.page = page;

        this.fileNameWithoutExtension = extractName(page.getGivenName());
        this.fileExtension = extractExtension(page.getGivenName(), defaultExtension);
        validateFileName(chapter.getGivenName());
        validateFileName(this.fileNameWithoutExtension);

        this.pageTitle = isIndex() ? "" : page.getHumanReadableName();
        this.pageMeta = new PageMeta(Collections.emptyMap());

        this.pageSectionIdTitles = new ArrayList<>();
    }

    public TocItem(String dirName, String fileNameWithOptionalExtension, String defaultExtension) {
        this(new TocNameAndOpts(dirName), new TocNameAndOpts(fileNameWithOptionalExtension), defaultExtension);
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapter.setHumanReadableName(chapterTitle);
    }

    public String getDirName() {
        return chapter.getGivenName();
    }

    public String getFileNameWithoutExtension() {
        return fileNameWithoutExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getChapterTitle() {
        return chapter.getHumanReadableName();
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

    public void setPageTitleIfNoTocOverridePresent(String pageTitle) {
        if (!page.hasTitleOverride()) {
            this.pageTitle = pageTitle;
        }
    }

    public boolean isIndex() {
        return chapter.getGivenName().isEmpty() && fileNameWithoutExtension.equals(INDEX);
    }

    public boolean match(String dirName, String fileNameWithoutExtension) {
        return getDirName().equals(dirName) && getFileNameWithoutExtension().equals(fileNameWithoutExtension);
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("chapterTitle", getChapterTitle());
        result.put("pageTitle", getPageTitle());
        result.put("pageMeta", pageMeta.toMap());
        result.put("dirName", getDirName());
        result.put("fileName", getFileNameWithoutExtension());
        result.put("fileExtension", getFileExtension());
        result.put("viewOnRelativePath", viewOnRelativePath);
        result.put("pageSectionIdTitles",
                getPageSectionIdTitles().stream().map(PageSectionIdTitle::toMap).collect(toList()));

        return result;
    }

    @Override
    public String toString() {
        return chapter.getGivenName() + "/" + fileNameWithoutExtension;
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

        return chapter.getGivenName().equals(tocItem.chapter.getGivenName()) &&
                fileNameWithoutExtension.equals(tocItem.fileNameWithoutExtension);
    }

    @Override
    public int hashCode() {
        int result = chapter.getGivenName().hashCode();
        result = 31 * result + fileNameWithoutExtension.hashCode();
        return result;
    }

    private void validateFileName(String name) {
        if (!fileNameAllowedPattern.matcher(name).matches()) {
            throw new IllegalArgumentException("file name should match: " + fileNameAllowedPattern + "\ngiven name: " +
                    name + "\n" +
                    "use\n---\ntitle: my custom title with any symbols like !#?\n---\nto override the title for your page");
        }
    }

    private static String extractExtension(String fileName, String defaultExtension) {
        int dotIdx = fileName.lastIndexOf('.');
        return dotIdx == -1 ? defaultExtension : fileName.substring(dotIdx + 1);
    }

    private static String extractName(String fileName) {
        int dotIdx = fileName.lastIndexOf('.');
        return dotIdx == -1 ? fileName : fileName.substring(0, dotIdx);
    }
}
