/*
 * Copyright 2025 znai maintainers
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

package org.testingisdocumenting.znai.search;

import org.testingisdocumenting.znai.core.DocMeta;
import org.testingisdocumenting.znai.markdown.PageMarkdownSection;
import org.testingisdocumenting.znai.structure.TocItem;

import java.util.Objects;

/**
 * global search entry
 */
public class GlobalSearchEntry {
    private String url;
    private String fullTitle;
    private String pageTitle;
    private String chapterTitle;
    private String pageSectionTitle;
    private SearchText text;

    public GlobalSearchEntry() {
    }

    public GlobalSearchEntry(DocMeta docMeta, TocItem tocItem, PageMarkdownSection section, String url) {
        this.url = url;
        this.fullTitle = buildFullTitle(tocItem, section, docMeta);
        this.pageTitle = tocItem.getPageTitle();
        this.chapterTitle = tocItem.getChapterTitle();
        this.pageSectionTitle = section.title();
        this.text = SearchScore.STANDARD.text(section.markdown());
    }

    private String buildFullTitle(TocItem tocItem, PageMarkdownSection section, DocMeta docMeta) {
        if (tocItem.isIndex()) {
            return docMeta.getTitle() + (section.title().isEmpty() ? "" : ": " + section.title());
        }

        String pageSectionPart = section.title().isEmpty() ?
                "" :
                ", " + section.title();

        String chapterPart = tocItem.getChapterTitle().isEmpty() ?
                "" :
                " [" + tocItem.getChapterTitle() + "]";

        return docMeta.getTitle() + ": " + tocItem.getPageTitle() + pageSectionPart + chapterPart;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GlobalSearchEntry that = (GlobalSearchEntry) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(fullTitle, that.fullTitle) &&
                Objects.equals(pageTitle, that.pageTitle) &&
                Objects.equals(chapterTitle, that.chapterTitle) &&
                Objects.equals(pageSectionTitle, that.pageSectionTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, fullTitle, pageTitle, chapterTitle, pageSectionTitle);
    }
}
