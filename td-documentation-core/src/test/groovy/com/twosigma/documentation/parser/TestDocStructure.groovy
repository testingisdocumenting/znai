package com.twosigma.documentation.parser

import com.twosigma.documentation.structure.DocStructure
import com.twosigma.documentation.structure.DocUrl

import java.nio.file.Path

/**
 * @author mykola
 */
class TestDocStructure implements DocStructure {
    private Set<String> validLinks = [] as Set

    @Override
    void validateLink(Path path, String sectionWithLinkTitle, DocUrl docUrl) {
        if (docUrl.isGlobalUrl()) {
            return
        }

        def urlBase = "${docUrl.dirName}/${docUrl.fileName}"
        def url = urlBase + (docUrl.pageSectionId.isEmpty() ? "" : "#${docUrl.pageSectionId}")

        if (! validLinks.contains(url.toString())) {
            throw new IllegalArgumentException("no valid link found in section '${sectionWithLinkTitle}': " + url)
        }
    }

    @Override
    String createLink(DocUrl docUrl) {
        if (docUrl.isGlobalUrl()) {
            return docUrl.url
        }

        def base = "/test-doc/${docUrl.dirName}/${docUrl.fileName}"
        return base + (docUrl.pageSectionId ? "#${docUrl.pageSectionId}" : "")
    }

    void addValidLink(String link) {
        validLinks.add(link)
    }
}
