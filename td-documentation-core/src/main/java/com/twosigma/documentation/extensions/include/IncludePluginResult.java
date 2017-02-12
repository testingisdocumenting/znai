package com.twosigma.documentation.extensions.include;

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
public class IncludePluginResult {
    private List<DocElement> docElements;

    private IncludePluginResult(DocElement docElements) {
        this.docElements = Collections.singletonList(docElements);
    }

    private IncludePluginResult(List<DocElement> docElements) {
        this.docElements = docElements;
    }

    public static IncludePluginResult docElements(Stream<DocElement> elements) {
        return new IncludePluginResult(elements.collect(Collectors.toList()));
    }

    public static IncludePluginResult reactComponent(final String name, final Map<String, Object> props) {
        DocElement customComponent = new DocElement(DocElementType.CUSTOM_COMPONENT);
        customComponent.addProp("componentName", name);
        customComponent.addProp("componentProps", props);

        return new IncludePluginResult(customComponent);
    }

    public List<DocElement> getDocElements() {
        return docElements;
    }
}
