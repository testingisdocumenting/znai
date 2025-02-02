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

package org.testingisdocumenting.znai.search;

import org.testingisdocumenting.znai.parser.PageSectionIdTitle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * search entry local to a single page section
 */
public class PageSearchEntry {
    private final PageSectionIdTitle pageSectionIdTitle;
    private final List<SearchText> searchTextList;

    public PageSearchEntry(PageSectionIdTitle pageSectionIdTitle, List<SearchText> searchTextList) {
        this.pageSectionIdTitle = pageSectionIdTitle;
        this.searchTextList = searchTextList;
    }

    public String getPageSectionId() {
        return pageSectionIdTitle.getId();
    }

    public String getPageSectionTitle() {
        return pageSectionIdTitle.getTitle();
    }

    public List<SearchText> getSearchTextList() {
        return searchTextList;
    }

    public String extractText() {
        return searchTextList.stream()
                .map(SearchText::getText)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.joining(" "));
    }
}
