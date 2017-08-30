package com.twosigma.documentation.structure;

import java.nio.file.Path;

/**
 * @author mykola
 */
public interface DocStructure {
    void validateLink(Path path, String sectionWithLinkTitle, DocUrl docUrl);
    String createLink(DocUrl docUrl);
}
