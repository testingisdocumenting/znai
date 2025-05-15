/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.website;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.core.DocMeta;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.structure.*;
import org.testingisdocumenting.znai.parser.MarkupParsingConfiguration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

class WebSiteDocStructure implements DocStructure {
    private final ComponentsRegistry componentsRegistry;
    private final DocMeta docMeta;
    private TableOfContents toc;
    private final MarkupParsingConfiguration parsingConfiguration;
    private final List<LinkToValidate> collectedExternalLinks;
    private final List<LinkToValidate> collectedLocalLinks;
    private final Map<String, GlobalAnchor> globalAnchorsById;
    private final Map<TocItem, List<String>> localAnchorIdsByTocItem;

    private final UniqueAnchorIdGenerator uniqueAnchorIdGenerator;

    WebSiteDocStructure(ComponentsRegistry componentsRegistry,
                        DocMeta docMeta,
                        TableOfContents toc,
                        MarkupParsingConfiguration parsingConfiguration) {
        this.componentsRegistry = componentsRegistry;
        this.docMeta = docMeta;
        this.toc = toc;
        this.parsingConfiguration = parsingConfiguration;
        this.collectedExternalLinks = new ArrayList<>();
        this.collectedLocalLinks = new ArrayList<>();
        this.globalAnchorsById = new HashMap<>();
        this.localAnchorIdsByTocItem = new HashMap<>();
        this.uniqueAnchorIdGenerator = new UniqueAnchorIdGenerator();
    }

    void removeGlobalAnchorsForPath(Path path) {
        List<Map.Entry<String, GlobalAnchor>> entriesForPath =
                globalAnchorsById.entrySet().stream().filter(kv -> kv.getValue().getFilePath().equals(path)).toList();
        entriesForPath.forEach(kv -> globalAnchorsById.remove(kv.getKey()));
    }

    void removeLocalAnchorsForTocItem(TocItem tocItem) {
        localAnchorIdsByTocItem.remove(tocItem);
    }

    void removeLinksForPath(Path path) {
        collectedExternalLinks.removeIf(linkToValidate -> linkToValidate.path.equals(path));
        collectedLocalLinks.removeIf(linkToValidate -> linkToValidate.path.equals(path));
        uniqueAnchorIdGenerator.resetCountersIfPresent(path);
    }

    void updateToc(TableOfContents toc) {
        this.toc = toc;
    }

    public void validateCollectedLinks() {
        String localLinksValidation = validateLocalLinks();
        String externalLinksValidation = validateExternalLinks();

        if (localLinksValidation.isEmpty() && externalLinksValidation.isEmpty()) {
            return;
        }

        String message = Stream.of(localLinksValidation, externalLinksValidation)
                .filter(m -> !m.isEmpty())
                .collect(joining("\n\n"));

        throw new RuntimeException(message + "\n");
    }

    @Override
    public void validateUrl(Path path, String additionalClue, DocUrl docUrl) {
        if (docUrl.isExternalUrl()) {
            if (componentsRegistry.docConfig().isValidateExternalLinks()) {
                collectedExternalLinks.add(new LinkToValidate(path, additionalClue, docUrl));
            }
        } else {
            collectedLocalLinks.add(new LinkToValidate(path, additionalClue, docUrl));
        }
    }

    @Override
    public String createUrl(Path path, DocUrl docUrl) {
        if (docUrl.isExternalUrl()) {
            return docUrl.getUrl();
        }

        if (docUrl.isFilePathBased()) {
            TocItem tocItem = findTocItemByDocUrlTocItemPath(docUrl);
            if (tocItem == null) {
                return docUrl.getTocItemFilePath();
            } else {
                docUrl.setResolvedToDirNameAndFileName(tocItem.getDirName(), tocItem.getFileNameWithoutExtension());
                return fullUrl(createRelativeUrl(path, docUrl) + docUrl.getAnchorIdWithHash());
            }
        }

        return fullUrl(createRelativeUrl(path, docUrl) + docUrl.getAnchorIdWithHash());
    }

    @Override
    public String fullUrl(String relativeUrl) {
        boolean addSlash = !relativeUrl.isEmpty() && !relativeUrl.startsWith("#");
        return  "/" + docMeta.getId() + (addSlash ? "/" : "") + relativeUrl;
    }

    @Override
    public void onSectionOrSubHeading(Path path, int level, String id) {
        uniqueAnchorIdGenerator.registerSectionOrSubHeading(path, level, id);
    }

    @Override
    public AnchorIds generateUniqueAnchors(Path path, String localId) {
        return uniqueAnchorIdGenerator.generateIds(path, localId);
    }

    @Override
    public void registerGlobalAnchor(Path sourcePath, String anchorId) {
        GlobalAnchor globalAnchor = globalAnchorsById.get(anchorId);

        if (globalAnchor != null) {
            ProgressReporter.reportWarning("global anchor <" + anchorId + "> specified in " + sourcePath +
                    " is already registered in " + globalAnchor.getFilePath());
        }

        TocItem tocItem = parsingConfiguration.tocItemByPath(componentsRegistry, toc, sourcePath);
        String url = createUrl(sourcePath, new DocUrl(tocItem.getDirName(), tocItem.getFileNameWithoutExtension(), anchorId));

        globalAnchorsById.put(anchorId, new GlobalAnchor(anchorId, sourcePath, url));
    }

    @Override
    public void registerLocalAnchors(Path path, AnchorIds anchorIds) {
        TocItem tocItem = parsingConfiguration.tocItemByPath(componentsRegistry, toc, path);
        if (tocItem == null) {
            throw new RuntimeException("Can't find TocItem associated with path: " + path);
        }

        List<String> anchors = localAnchorIdsByTocItem.computeIfAbsent(tocItem, k -> new ArrayList<>());
        anchors.add(anchorIds.main());
        anchors.addAll(anchorIds.additional());
    }

    @Override
    public String globalAnchorUrl(Path clientPath, String anchorId) {
        GlobalAnchor anchor = globalAnchorsById.get(anchorId);
        if (anchor == null) {
            throw new RuntimeException("cannot find global anchor <" + anchorId +
                    "> referenced in " + clientPath + ".\nMake sure you call globalAnchorUrl in lazy evaluated manner" +
                    " to make sure all the global references were registered");
        }

        TocItem tocItem = parsingConfiguration.tocItemByPath(componentsRegistry, toc, anchor.getFilePath());
        return createUrl(anchor.getFilePath(), new DocUrl(tocItem.getDirName(), tocItem.getFileNameWithoutExtension(), anchorId));
    }

    @Override
    public Optional<String> findGlobalAnchorUrl(String globalAnchorId) {
        GlobalAnchor anchor = globalAnchorsById.get(globalAnchorId);
        return anchor == null ? Optional.empty() : Optional.of(anchor.getUrl());
    }

    @Override
    public TableOfContents tableOfContents() {
        return toc;
    }

    private String validateLocalLinks() {
        return collectedLocalLinks.stream()
                .map(this::validateLocalLink)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(joining("\n\n"));
    }

    private String validateExternalLinks() {
        return collectedExternalLinks.parallelStream()
                .map(this::validateExternalLink)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(joining("\n\n"));
    }

    private Optional<String> validateLocalLink(LinkToValidate link) {
        String anchorId = link.docUrl.getAnchorId();

        TocItem tocItem = findTocItemByLink(link);

        if (tocItem == null) {
            return Optional.of(createInvalidLinkMessageTocNotFound(link));
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

        return Optional.of(createInvalidAnchorMessage(tocItem, link));
    }

    private Optional<String> validateExternalLink(LinkToValidate linkToValidate) {
        String url = linkToValidate.docUrl.getUrl();

        int responseCode = pingUrlConnection(url, 5000);
        if ((responseCode >= 200 && responseCode <= 204) || responseCode == 301) {
            return Optional.empty();
        }

        return Optional.of("can't get data from " + url + ": " +
                checkFileMessage(linkToValidate));
    }

    private int pingUrlConnection(String url, int timeout) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(timeout);
            connection.setConnectTimeout(timeout);
            connection.addRequestProperty("User-Agent", "Mozilla");

            return connection.getResponseCode();
        } catch (IOException e) {
            return -1;
        }
    }

    private TocItem findTocItemByDocUrlTocItemPath(DocUrl docUrl) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        if (resourcesResolver.canResolve(docUrl.getTocItemFilePath())) {
            Path tocItemPath = resourcesResolver.fullPath(docUrl.getTocItemFilePath()).toAbsolutePath().normalize();
            return parsingConfiguration.tocItemByPath(componentsRegistry, toc, tocItemPath);
        } else {
            return null;
        }
    }

    private TocItem findTocItemByLink(LinkToValidate link) {
        if (link.docUrl.isIndexUrl()) {
            return toc.getIndex();
        }

        if (link.docUrl.isAnchorOnly()) {
            return parsingConfiguration.tocItemByPath(componentsRegistry, toc, link.path);
        }

        if (link.docUrl.isFilePathBased()) {
            return findTocItemByDocUrlTocItemPath(link.docUrl);
        }

        return toc.findTocItem(link.docUrl.getDirName(), link.docUrl.getFileNameWithoutExtension());
    }

    private String createInvalidLinkMessageTocNotFound(LinkToValidate link) {
        String checkFileMessage = checkFileMessage(link);

        if (link.docUrl.isAnchorOnly()) {
            return "can't find the anchor " + link.docUrl.getAnchorIdWithHash() + checkFileMessage;
        }

        return "can't find a TOC registered page associated with: " + link.url() + checkFileMessage;
    }

    private String createInvalidAnchorMessage(TocItem foundTocItem, LinkToValidate link) {
        String checkFileMessage = checkFileMessage(link);
        return "can't find an anchor " + link.docUrl().getAnchorIdWithHash() + " in: " + foundTocItem.getFilePath() + checkFileMessage;
    }

    private String checkFileMessage(LinkToValidate link) {
        return "\nreferenced in file: " + link.path + (
                link.additionalClue.isEmpty() ? "" : ", " + link.additionalClue);
    }

    private boolean isValidGlobalAnchor(TocItem tocItemWithAnchor, String anchorId) {
        GlobalAnchor globalAnchor = globalAnchorsById.get(anchorId);
        if (globalAnchor == null) {
            return false;
        }

        TocItem anchorTocItem = parsingConfiguration.tocItemByPath(componentsRegistry, toc, globalAnchor.getFilePath());
        return tocItemWithAnchor.equals(anchorTocItem);
    }

    private boolean isValidLocalAnchor(TocItem tocItem, String anchorId) {
        List<String> localIds = localAnchorIdsByTocItem.get(tocItem);
        return localIds != null && localIds.contains(anchorId);
    }

    private String createRelativeUrl(Path path, DocUrl docUrl) {
        if (docUrl.isAnchorOnly()) {
            TocItem tocItem = parsingConfiguration.tocItemByPath(componentsRegistry, toc, path);

            // most likely from within footer
            if (tocItem == null) {
                throw new RuntimeException("can't use relative url in this context, url: #" + docUrl.getAnchorId());
            }
            if (tocItem.isIndex()) {
                return "";
            }

            return tocItem.getDirName() + "/" + tocItem.getFileNameWithoutExtension();
        }

        return docUrl.isIndexUrl() ? "" : docUrl.getDirName() + "/" + docUrl.getFileNameWithoutExtension();
    }

    private record LinkToValidate(Path path, String additionalClue, DocUrl docUrl) {
        public String url() {
            return docUrl.isFilePathBased() ?
                    docUrl.getTocItemFilePath() :
                    docUrl.getDirName() + "/" + docUrl.getFileNameWithoutExtension() + docUrl.getAnchorIdWithHash();
        }
    }
}
