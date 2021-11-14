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

package org.testingisdocumenting.znai.parser

import org.testingisdocumenting.znai.structure.DocStructure
import org.testingisdocumenting.znai.structure.DocUrl
import org.testingisdocumenting.znai.structure.GlobalAnchor
import org.testingisdocumenting.znai.structure.TableOfContents

import java.nio.file.Path

class TestDocStructure implements DocStructure {
    private Set<String> validLinks = [] as Set
    private TableOfContents toc = new TableOfContents()

    Set<String> registeredLocalLinks = [] as TreeSet

    @Override
    void validateUrl(Path path, String additionalClue, DocUrl docUrl) {
        if (docUrl.isExternalUrl()) {
            return
        }

        def urlBase = "${docUrl.dirName}/${docUrl.fileName}"
        def url = urlBase + (docUrl.anchorId.isEmpty() ? "" : "#${docUrl.anchorId}")

        if (! validLinks.contains(url.toString())) {
            throw new IllegalArgumentException("no valid link found in ${path.fileName}, ${additionalClue}: " + url)
        }
    }

    @Override
    String createUrl(Path path, DocUrl docUrl) {
        if (docUrl.isExternalUrl()) {
            return docUrl.url
        }

        def base = "${docUrl.dirName}/${docUrl.fileName}"
        return fullUrl(base + (docUrl.anchorId ? "#${docUrl.anchorId}" : ""))
    }

    @Override
    String fullUrl(String relativeUrl) {
        return  "/test-doc/" + relativeUrl
    }

    @Override
    void registerGlobalAnchor(Path sourcePath, String anchorId) {

    }

    @Override
    void registerLocalAnchor(Path path, String anchorId) {
        registeredLocalLinks.add(anchorId)
    }

    @Override
    String globalAnchorUrl(Path clientPath, String anchorId) {
        return null
    }

    @Override
    Map<String, String> globalAnchors() {
        return [:]
    }

    void setToc(TableOfContents testToc) {
        this.toc = testToc
    }

    @Override
    TableOfContents tableOfContents() {
        return toc
    }

    void clear() {
        validLinks.clear()
        registeredLocalLinks.clear()
    }

    void addValidLink(String link) {
        validLinks.add(link)
    }
}
