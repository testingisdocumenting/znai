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

import java.time.Instant;
import java.util.List;

import org.testingisdocumenting.znai.parser.PageSectionIdTitle;
import org.testingisdocumenting.znai.parser.docelement.DocElement;

import static org.testingisdocumenting.znai.parser.docelement.DocElementType.SECTION;
import static java.util.stream.Collectors.toList;

public class Page {
    private final DocElement docElement;
    private final List<PageSectionIdTitle> pageSectionIdTitles;
    private final Instant lastModifiedTime;
    private final PageMeta pageMeta;

    public Page(DocElement docElement, Instant lastModifiedTime, PageMeta pageMeta) {
        this.docElement = docElement;
        this.pageSectionIdTitles = extractFirstLevelHeadings(docElement);
        this.lastModifiedTime = lastModifiedTime;
        this.pageMeta = pageMeta;
    }

    public DocElement getDocElement() {
        return docElement;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public PageMeta getPageMeta() {
        return pageMeta;
    }

    public List<PageSectionIdTitle> getPageSectionIdTitles() {
        return pageSectionIdTitles;
    }

    private List<PageSectionIdTitle> extractFirstLevelHeadings(final DocElement docElement) {
        return docElement.getContent().stream().
            filter(e -> e.getType().equals(SECTION)).
            map(this::createSectionIdTitle).
            collect(toList());
    }

    private PageSectionIdTitle createSectionIdTitle(DocElement docElement) {
        String title = docElement.getProp("title").toString();
        return new PageSectionIdTitle(title);
    }
}
