package com.twosigma.documentation.parser;

import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Path;
import java.util.List;

/**
 * @author mykola
 */
public class MarkupParserResult {
    private DocElement docElement;
    private List<Path> filesMarkupDependsOn;

    public MarkupParserResult(DocElement docElement, List<Path> filesMarkupDependsOn) {
        this.docElement = docElement;
        this.filesMarkupDependsOn = filesMarkupDependsOn;
    }

    public DocElement getDocElement() {
        return docElement;
    }

    public List<Path> getFilesMarkupDependsOn() {
        return filesMarkupDependsOn;
    }
}
