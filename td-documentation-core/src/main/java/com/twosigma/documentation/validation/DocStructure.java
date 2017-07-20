package com.twosigma.documentation.validation;

import java.nio.file.Path;

/**
 * @author mykola
 */
public interface DocStructure {
    void validateLink(Path path, String sectionWithLinkTitle, String dirName, String fileName, String pageSectionId);
    String createLink(String dirName, String fileName, String pageSectionId);
}
