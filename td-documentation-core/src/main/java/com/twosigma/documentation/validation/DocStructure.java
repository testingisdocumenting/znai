package com.twosigma.documentation.validation;

/**
 * @author mykola
 */
public interface DocStructure {
    void validateLink(String dirName, String fileName, String pageSectionId);
    String createLink(String dirName, String fileName, String pageSectionId);
}
