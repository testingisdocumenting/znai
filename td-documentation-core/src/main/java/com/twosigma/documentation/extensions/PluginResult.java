package com.twosigma.documentation.extensions;

import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class PluginResult {
    private List<DocElement> docElements;

    private PluginResult(DocElement docElements) {
        this.docElements = Collections.singletonList(docElements);
    }

    private PluginResult(List<DocElement> docElements) {
        this.docElements = docElements;
    }

    public static PluginResult docElements(Stream<DocElement> elements) {
        return new PluginResult(elements.collect(Collectors.toList()));
    }

    public static PluginResult docElement(DocElement element) {
        return new PluginResult(element);
    }

    public static PluginResult docElement(String type, Map<String, ?> props) {
        DocElement docElement = new DocElement(type);
        props.forEach(docElement::addProp);

        return new PluginResult(docElement);
    }

    public List<DocElement> getDocElements() {
        return docElements;
    }
}
