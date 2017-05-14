package com.twosigma.documentation.parser;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

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

    /**
     * Top level page element. use get content to get access to the children
     * @return top level page element
     */
    public DocElement getDocElement() {
        return docElement;
    }

    public List<Map<String, Object>> contentToListOfMaps() {
        return docElement.getContent().stream().map(DocElement::toMap).collect(toList());
    }

    public List<AuxiliaryFile> getAuxiliaryFiles() {
        return auxiliaryFiles;
    }
}
