package com.twosigma.documentation.structure;

import java.nio.file.Path;

public interface DocStructure {
    void validateUrl(Path path, String sectionWithLinkTitle, DocUrl docUrl);
    String createUrl(DocUrl docUrl);
    String fullUrl(String relativeUrl);

    void registerGlobalAnchor(Path sourcePath, String anchorId);
    void registerLocalAnchor(Path path, String anchorId);
    String globalAnchorUrl(Path clientPath, String anchorId);
}
