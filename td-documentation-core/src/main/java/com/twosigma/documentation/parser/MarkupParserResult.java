package com.twosigma.documentation.parser;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.search.PageSearchEntry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class MarkupParserResult {
    private DocElement docElement;
    private List<AuxiliaryFile> auxiliaryFiles;
    private Map<String, List<String>> properties;
    private List<PageSearchEntry> searchEntries;

    public MarkupParserResult(DocElement docElement, List<PageSearchEntry> searchEntries,
                              List<AuxiliaryFile> auxiliaryFiles, Map<String, List<String>> properties) {
        this.docElement = docElement;
        this.searchEntries = searchEntries;
        this.auxiliaryFiles = auxiliaryFiles;
        this.properties = new LinkedHashMap<>(properties);
    }

    /**
     * Top level page element. use get content to get access to the children
     * @return top level page element
     */
    public DocElement getDocElement() {
        return docElement;
    }

    public Map<String, List<String>> getProperties() {
        return properties;
    }

    public List<PageSearchEntry> getSearchEntries() {
        return searchEntries;
    }

    public List<Map<String, Object>> contentToListOfMaps() {
        return docElement.getContent().stream().map(DocElement::toMap).collect(toList());
    }

    public List<AuxiliaryFile> getAuxiliaryFiles() {
        return auxiliaryFiles;
    }
}
