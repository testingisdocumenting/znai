package com.twosigma.documentation.structure;

import com.twosigma.documentation.parser.docelement.DocElement;

/**
 * @author mykola
 */
public class Footer {
    private final DocElement docElement;

    public Footer(DocElement docElement) {
        this.docElement = docElement;
    }

    public DocElement getDocElement() {
        return docElement;
    }
}
