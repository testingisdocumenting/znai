package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.parser.docelement.DocElement;

import java.util.List;

/**
 * @author mykola
 */
class JavaIncludeResult {
    private final List<DocElement> docElements;
    private final String text;

    public JavaIncludeResult(List<DocElement> docElements, String text) {
        this.docElements = docElements;
        this.text = text;
    }

    public List<DocElement> getDocElements() {
        return docElements;
    }

    public String getText() {
        return text;
    }
}
