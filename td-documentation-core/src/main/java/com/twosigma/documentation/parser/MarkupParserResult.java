package com.twosigma.documentation.parser;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.search.PageSearchEntry;
import com.twosigma.documentation.structure.PageMeta;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class MarkupParserResult {
    private DocElement docElement;
    private List<String> globalAnchorIds;
    private List<AuxiliaryFile> auxiliaryFiles;
    private PageMeta pageMeta;
    private List<PageSearchEntry> searchEntries;

    public MarkupParserResult(DocElement docElement,
                              List<String> globalAnchorIds,
                              List<PageSearchEntry> searchEntries,
                              List<AuxiliaryFile> auxiliaryFiles,
                              PageMeta pageMeta) {
        this.docElement = docElement;
        this.globalAnchorIds = globalAnchorIds;
        this.searchEntries = searchEntries;
        this.auxiliaryFiles = auxiliaryFiles;
        this.pageMeta = pageMeta;
    }

    /**
     * Top level page element. use get content to get access to the children
     *
     * @return top level page element
     */
    public DocElement getDocElement() {
        return docElement;
    }

    public List<String> getGlobalAnchorIds() {
        return globalAnchorIds;
    }

    public PageMeta getPageMeta() {
        return pageMeta;
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

    public String getAllText() {
        return searchEntries.stream().map(se -> se.getText().getText()).collect(joining(" "));
    }
}
