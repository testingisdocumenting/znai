package com.twosigma.documentation.website;

import com.twosigma.documentation.structure.*;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

import static com.twosigma.documentation.website.ProgressReporter.reportPhase;
import static java.util.stream.Collectors.joining;

class WebSiteDocStructure implements DocStructure {
    private final DocMeta docMeta;
    private TableOfContents toc;
    private final List<LinkToValidate> linksToValidate;
    private final Map<String, Path> globalAnchorPathById;

    WebSiteDocStructure(DocMeta docMeta, TableOfContents toc) {
        this.docMeta = docMeta;
        this.toc = toc;
        this.linksToValidate = new ArrayList<>();
        this.globalAnchorPathById = new HashMap<>();
    }

    public void validateCollectedLinks() {
        reportPhase("validating links");
        String validationErrorMessage = linksToValidate.stream().map(this::validateLink)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(joining("\n\n"));

        if (!validationErrorMessage.isEmpty()) {
            throw new IllegalArgumentException(validationErrorMessage  + "\n");
        }
    }

    @Override
    public void validateUrl(Path path, String sectionWithLinkTitle, DocUrl docUrl) {
        if (docUrl.isGlobalUrl() || docUrl.isIndexUrl()) {
            return;
        }

        linksToValidate.add(new LinkToValidate(path, sectionWithLinkTitle, docUrl));
    }


    @Override
    public String createUrl(DocUrl docUrl) {
        if (docUrl.isGlobalUrl()) {
            return docUrl.getUrl();
        }

        if (docUrl.isIndexUrl()) {
            return "/" + docMeta.getId();
        }

        String base = "/" + docMeta.getId() + "/" + docUrl.getDirName() + "/" + docUrl.getFileName();
        return base + (docUrl.getAnchorId().isEmpty() ? "" : "#" + docUrl.getAnchorId());
    }

    @Override
    public String prefixUrlWithProductId(String url) {
        url = url.toLowerCase();
        if (url.startsWith("http")) {
            return url;
        }

        return "/" + docMeta.getId() + "/" + url;
    }

    @Override
    public void registerGlobalAnchor(Path sourcePath, String anchorId) {
        Path existingPath = globalAnchorPathById.get(anchorId);

        if (existingPath != null) {
            throw new RuntimeException("global anchor <" + anchorId + "> specified in " + sourcePath +
                    " is already registered in " + existingPath);
        }

        globalAnchorPathById.put(anchorId, sourcePath);
    }

    @Override
    public String globalAnchorUrl(Path clientPath, String anchorId) {
        Path anchorPath = globalAnchorPathById.get(anchorId);
        if (anchorPath == null) {
            throw new RuntimeException("cannot find global anchor <" + anchorId + "> referenced in " + clientPath);
        }

        TocItem tocItem = toc.tocItemByPath(anchorPath);
        return createUrl(new DocUrl(tocItem.getDirName(), tocItem.getFileNameWithoutExtension(), anchorId));
    }

    private Optional<String> validateLink(LinkToValidate link) {
        String anchorId = link.docUrl.getAnchorId();

        String url = link.docUrl.getDirName() + "/" + link.docUrl.getFileName() +
                (anchorId.isEmpty() ?  "" : "#" + anchorId);

        Supplier<String> validationMessage = () -> "can't find a page associated with: " + url +
                "\ncheck file: " + link.path + ", section title: " + link.sectionWithLinkTitle;

        TocItem tocItem = toc.findTocItem(link.docUrl.getDirName(), link.docUrl.getFileName());
        if (tocItem == null) {
            return Optional.of(validationMessage.get());
        }

        if (anchorId.isEmpty()) {
            return Optional.empty();
        }

        if (isValidGlobalAnchor(tocItem, anchorId)) {
            return Optional.empty();
        }

        if (!tocItem.hasPageSection(anchorId)) {
            return Optional.of(validationMessage.get());
        }

        return Optional.empty();
    }

    private boolean isValidGlobalAnchor(TocItem tocItemWithAnchor, String anchorId) {
        Path anchorPath = globalAnchorPathById.get(anchorId);
        if (anchorPath == null) {
            return false;
        }

        TocItem anchorTocItem = toc.tocItemByPath(anchorPath);
        return tocItemWithAnchor.equals(anchorTocItem);
    }

    private class LinkToValidate {
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
