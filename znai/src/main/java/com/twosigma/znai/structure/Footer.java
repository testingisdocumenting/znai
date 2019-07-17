package com.twosigma.znai.structure;

import com.twosigma.znai.parser.docelement.DocElement;

public class Footer {
    private final DocElement docElement;

    public Footer(DocElement docElement) {
        this.docElement = docElement;
    }

    public DocElement getDocElement() {
        return docElement;
    }
}
