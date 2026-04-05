/*
 * Copyright 2026 znai maintainers
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
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.structure.DocUrl;
import org.testingisdocumenting.znai.structure.TocItem;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class GlobalSearchEntriesBuilder {
    private final DocMeta docMeta;
    private final DocStructure docStructure;
    private final GlobalSearchEntries globalSearchEntries;

    public GlobalSearchEntriesBuilder(DocMeta docMeta, DocStructure docStructure) {
        this.docMeta = docMeta;
        this.docStructure = docStructure;
        this.globalSearchEntries = new GlobalSearchEntries();
    }

    public GlobalSearchEntries getGlobalSearchEntries() {
        return globalSearchEntries;
    }

    public void addSearchEntries(TocItem tocItem, List<PageMarkdownSection> sections) {
        List<GlobalSearchEntry> entries = sections.stream()
                .filter(section -> !(section.title().isEmpty() && section.markdown().trim().isEmpty()))
                .map(section -> new GlobalSearchEntry(
                        docMeta,
                        tocItem,
                        section,
                        searchEntryUrl(tocItem, section)))
                .collect(toList());

        globalSearchEntries.addAll(entries);
    }

    String searchEntryUrl(TocItem tocItem, PageMarkdownSection section) {
        if (tocItem.isIndex()) {
            String anchorIdWithHash = section.id().isEmpty() ? "" : "#" + section.id();
            return docStructure.fullUrl(anchorIdWithHash);
        }

        DocUrl docUrl = new DocUrl(tocItem.getDirName(), tocItem.getFileNameWithoutExtension(), section.id());
        return docStructure.createUrl(null, docUrl);
    }
}
