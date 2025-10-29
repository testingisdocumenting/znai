/*
 * Copyright 2025 znai maintainers
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

package org.testingisdocumenting.znai.website;

import org.testingisdocumenting.znai.core.DocMeta;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.structure.Page;
import org.testingisdocumenting.znai.structure.TocItem;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LlmContentGenerator {
    private final DocMeta docMeta;
    private final String llmUrlPrefix;
    private final Map<TocItem, Page> pageByTocItem;
    private final Map<TocItem, MarkupParserResult> parserResultByTocItem;

    public LlmContentGenerator(DocMeta docMeta,
                               String llmUrlPrefix,
                               Map<TocItem, Page> pageByTocItem,
                               Map<TocItem, MarkupParserResult> parserResultByTocItem) {
        this.docMeta = docMeta;
        this.llmUrlPrefix = llmUrlPrefix;
        this.pageByTocItem = pageByTocItem;
        this.parserResultByTocItem = parserResultByTocItem;
    }

    public String generateContent() {
        StringBuilder llmContent = new StringBuilder();
        llmContent.append("\"").append(docMeta.getTitle()).append("\" full guide:\n\n");

        pageByTocItem.forEach((tocItem, page) -> {
            if (page == null) {
                return;
            }

            MarkupParserResult parserResult = parserResultByTocItem.get(tocItem);
            if (parserResult == null || parserResult.markdown() == null) {
                return;
            }

            String basePageUrl = buildPageUrl(tocItem);

            parserResult.markdown().sections().forEach(section -> {
                if (section.title().isEmpty() && section.markdown().trim().isEmpty()) {
                    return;
                }

                llmContent.append("# ").append(tocItem.getChapterTitle()).append(" :: ").append(tocItem.getPageTitle());

                if (!section.title().isEmpty()) {
                    llmContent.append(" :: ").append(section.title());
                }
                llmContent.append("\n");

                String sectionUrl = section.title().isEmpty()
                    ? basePageUrl
                    : basePageUrl + "#" + section.id();
                llmContent.append("answer-link: ").append(sectionUrl).append("\n\n");

                llmContent.append(section.markdown());
                llmContent.append("\n\n");
            });
        });

        return llmContent.toString();
    }

    private String buildPageUrl(TocItem tocItem) {
        return Stream.of(llmUrlPrefix,
                        docMeta.getId(),
                        tocItem.getDirName(),
                        tocItem.getFileNameWithoutExtension())
                .filter(part -> !part.isEmpty())
                .collect(Collectors.joining("/"));
    }
}
