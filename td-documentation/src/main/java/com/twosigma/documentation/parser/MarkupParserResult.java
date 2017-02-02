package com.twosigma.documentation.parser;

import com.twosigma.documentation.AuxiliaryFile;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Path;
import java.util.List;

/**
 * @author mykola
 */
public class MarkupParserResult {
    private DocElement docElement;
    private List<AuxiliaryFile> auxiliaryFiles;

    public MarkupParserResult(DocElement docElement, List<AuxiliaryFile> auxiliaryFiles) {
        this.docElement = docElement;
        this.auxiliaryFiles = auxiliaryFiles;
    }

    public DocElement getDocElement() {
        return docElement;
    }

    public List<AuxiliaryFile> getAuxiliaryFiles() {
        return auxiliaryFiles;
    }
}
