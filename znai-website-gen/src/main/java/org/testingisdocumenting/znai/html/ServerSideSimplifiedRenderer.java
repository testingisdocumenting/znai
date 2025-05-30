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

package org.testingisdocumenting.znai.html;

import org.testingisdocumenting.znai.search.PageLocalSearchEntries;
import org.testingisdocumenting.znai.search.PageSearchEntry;
import org.testingisdocumenting.znai.structure.TableOfContents;
import org.testingisdocumenting.znai.structure.TocItem;
import org.testingisdocumenting.znai.utils.ResourceUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.stream.Collectors;

public class ServerSideSimplifiedRenderer {
    static final String LOADING_INDICATOR = ResourceUtils.textContent("template/initial-page-loading.html");

    public static String renderToc(TableOfContents toc, String docId) {
        return section("table-of-contents",
                toc.getTocItems().stream()
                        .filter((tocItem -> !tocItem.isIndex()))
                        .map((tocItem) -> ServerSideSimplifiedRenderer.renderTocLink(tocItem, docId))
                        .collect(Collectors.joining("\n")));
    }

    public static String renderPageTextContent(PageLocalSearchEntries pageSearchEntries) {
        return LOADING_INDICATOR + section("page-content",
                pageSearchEntries.entries().stream()
                        .map(ServerSideSimplifiedRenderer::renderPageEntry)
                        .collect(Collectors.joining("\n")));
    }

    private static String renderTocLink(TocItem tocItem, String docId) {
        String rootLink = aHref("/" + docId + "/" + tocItem.getDirName() + "/" + tocItem.getFileNameWithoutExtension() + "/",
                tocItem.getPageTitle());

        return article(rootLink);
    }

    private static String renderPageEntry(PageSearchEntry entry) {
        String optionalHeader = entry.getPageSectionTitle().isEmpty() ?
                "" :
                "<header><h1>" + entry.getPageSectionTitle() + "</h1></header>\n";

        String paragraph = "<p>" + StringEscapeUtils.escapeHtml4(entry.extractText()) + "</p>\n";

        return article(optionalHeader + paragraph);
    }

    private static String section(String id, String htmlBlock) {
        return "<section id=\"" + id + "\" style=\"max-width: 640px; margin-left: auto; margin-right: auto;\">\n" +
                htmlBlock +
                "</section>\n";
    }

    private static String article(String htmlBlock) {
        return "<article>\n" + htmlBlock + "</article>\n";
    }

    private static String aHref(String url, String title) {
        return "<a href=\"" + url + "\">" + title + "</a>\n";
    }
}
