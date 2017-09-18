package com.twosigma.documentation.parser.sphinx;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElementCreationParserHandler;

import java.nio.file.Path;
import java.util.Collections;

/**
 * @author mykola
 */
public class SphinxDocTreeParser implements MarkupParser {
    private ComponentsRegistry componentsRegistry;

    public SphinxDocTreeParser(ComponentsRegistry componentsRegistry) {
        this.componentsRegistry = componentsRegistry;
    }

    @Override
    public MarkupParserResult parse(Path path, String docXml) {
        DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(componentsRegistry, path);

        DocTreeDomXmlParser xmlParser = new DocTreeDomXmlParser(parserHandler);
        xmlParser.parse(docXml);

        return new MarkupParserResult(parserHandler.getDocElement(), parserHandler.getAuxiliaryFiles(),
                Collections.emptyMap());
    }
}
