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

package com.twosigma.znai.website;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.structure.*;
import com.twosigma.znai.parser.MarkupParsingConfiguration;

import java.nio.file.Path;
import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class WebSiteDocStructure implements DocStructure {
    private final ComponentsRegistry componentsRegistry;
    private final DocMeta docMeta;
    private final TableOfContents toc;
    private final MarkupParsingConfiguration parsingConfiguration;
    private final List<LinkToValidate> linksToValidate;
    private final Map<String, Path> globalAnchorPathById;
    private final Map<TocItem, List<String>> localAnchorIdsByTocItem;

    WebSiteDocStructure(ComponentsRegistry componentsRegistry,
                        DocMeta docMeta,
                        TableOfContents toc,
                        MarkupParsingConfiguration parsingConfiguration) {
        this.componentsRegistry = componentsRegistry;
        this.docMeta = docMeta;
        this.toc = toc;
        this.parsingConfiguration = parsingConfiguration;
        this.linksToValidate = new ArrayList<>();
        this.globalAnchorPathById = new HashMap<>();
        this.localAnchorIdsByTocItem = new HashMap<>();
    }

    void removeGlobalAnchorsForPath(Path path) {
        List<Map.Entry<String, Path>> entriesForPath =
                globalAnchorPathById.entrySet().stream().filter(kv -> kv.getValue().equals(path)).collect(toList());
        entriesForPath.forEach(kv -> globalAnchorPathById.remove(kv.getKey()));
    }

    void removeLocalAnchorsForTocItem(TocItem tocItem) {
        localAnchorIdsByTocItem.remove(tocItem);
    }

    void removeLinksForPath(Path path) {
        linksToValidate.removeIf(linkToValidate -> linkToValidate.path.equals(path));
    }

    public void validateCollectedLinks() {
        String validationErrorMessage = linksToValidate.stream()
                .map(this::validateLink)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(joining("\n\n"));

        if (!validationErrorMessage.isEmpty()) {
            throw new IllegalArgumentException(validationErrorMessage + "\n");
        }
    }

    @Override
    public void validateUrl(Path path, String sectionWithLinkTitle, DocUrl docUrl) {
        if (docUrl.isExternalUrl() || docUrl.isIndexUrl()) {
            return;
        }

        linksToValidate.add(new LinkToValidate(path, sectionWithLinkTitle, docUrl));
    }

    @Override
    public String createUrl(Path path, DocUrl docUrl) {
        if (docUrl.isExternalUrl()) {
            return docUrl.getUrl();
        }

        if (docUrl.isIndexUrl()) {
            return "/" + docMeta.getId();
        }

        return fullUrl(createUrlBase(path, docUrl) + docUrl.getAnchorIdWithHash());
    }

    @Override
    public String fullUrl(String relativeUrl) {
        return  "/" + docMeta.getId() + "/" + relativeUrl;
    }

    @Override
    public void registerGlobalAnchor(Path sourcePath, String anchorId) {
        Path existingPath = globalAnchorPathById.get(anchorId);

        if (existingPath != null) {
            ProgressReporter.reportWarning("global anchor <" + anchorId + "> specified in " + sourcePath +
                    " is already registered in " + existingPath);
        }

        globalAnchorPathById.put(anchorId, sourcePath);
    }

    @Override
    public void registerLocalAnchor(Path path, String anchorId) {
        TocItem tocItem = parsingConfiguration.tocItemByPath(componentsRegistry, toc, path);
        if (tocItem == null) {
            throw new RuntimeException("Can't find TocItem associated with path: " + path);
        }

        List<String> anchors = localAnchorIdsByTocItem.computeIfAbsent(tocItem, k -> new ArrayList<>());
        anchors.add(anchorId);
    }

    @Override
    public String globalAnchorUrl(Path clientPath, String anchorId) {
        Path anchorPath = globalAnchorPathById.get(anchorId);
        if (anchorPath == null) {
            throw new RuntimeException("cannot find global anchor <" + anchorId + "> referenced in " + clientPath);
        }

        TocItem tocItem = parsingConfiguration.tocItemByPath(componentsRegistry, toc, anchorPath);
        return createUrl(anchorPath, new DocUrl(tocItem.getDirName(), tocItem.getFileNameWithoutExtension(), anchorId));
    }

    @Override
    public TableOfContents tableOfContents() {
        return toc;
    }

    private Optional<String> validateLink(LinkToValidate link) {
        String anchorId = link.docUrl.getAnchorId();

        TocItem tocItem = link.docUrl.isAnchorOnly() ?
                parsingConfiguration.tocItemByPath(componentsRegistry, toc, link.path):
                toc.findTocItem(link.docUrl.getDirName(), link.docUrl.getFileName());
        if (tocItem == null) {
            return Optional.of(createInvalidLinkMessage(link));
        }

        if (anchorId.isEmpty()) {
            return Optional.empty();
        }

        if (isValidGlobalAnchor(tocItem, anchorId)) {
            return Optional.empty();
        }

        if (isValidLocalAnchor(tocItem, anchorId)) {
            return Optional.empty();
        }

        return Optional.of(createInvalidLinkMessage(link));
    }

    private String createInvalidLinkMessage(LinkToValidate link) {
        String checkFileMessage = "\ncheck file: " + link.path + ", section title: " + link.sectionWithLinkTitle;

        if (link.docUrl.isAnchorOnly()) {
            return "can't find the anchor " + link.docUrl.getAnchorIdWithHash() + checkFileMessage;
        }

        String url = link.docUrl.getDirName() + "/" + link.docUrl.getFileName() + link.docUrl.getAnchorIdWithHash();
        return "can't find a page associated with: " + url + checkFileMessage;
    }

    private boolean isValidGlobalAnchor(TocItem tocItemWithAnchor, String anchorId) {
        Path anchorPath = globalAnchorPathById.get(anchorId);
        if (anchorPath == null) {
            return false;
        }

        TocItem anchorTocItem = parsingConfiguration.tocItemByPath(componentsRegistry, toc, anchorPath);
        return tocItemWithAnchor.equals(anchorTocItem);
    }

    private boolean isValidLocalAnchor(TocItem tocItem, String anchorId) {
        List<String> localIds = localAnchorIdsByTocItem.get(tocItem);
        return localIds != null && localIds.contains(anchorId);
    }

    private String createUrlBase(Path path, DocUrl docUrl) {
        if (docUrl.isAnchorOnly()) {
            TocItem tocItem = parsingConfiguration.tocItemByPath(componentsRegistry, toc, path);

            return tocItem == null ?
                    "<should not happen>":
                    tocItem.getDirName() + "/" + tocItem.getFileNameWithoutExtension();
        }

        return docUrl.getDirName() + "/" + docUrl.getFileName();
    }

    private static class LinkToValidate {
        private final Path path;
        private final String sectionWithLinkTitle;
        private final DocUrl docUrl;

        LinkToValidate(Path path, String sectionWithLinkTitle, DocUrl docUrl) {
            this.path = path;
            this.sectionWithLinkTitle = sectionWithLinkTitle;
            this.docUrl = docUrl;
        }
    }
}
