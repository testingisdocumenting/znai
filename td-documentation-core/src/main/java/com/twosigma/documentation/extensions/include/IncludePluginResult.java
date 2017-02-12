package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.util.Map;

/**
 * @author mykola
 */
public class IncludePluginResult {
    private DocElement docElement;

    private IncludePluginResult(DocElement docElement) {
        this.docElement = docElement;
    }

    public static IncludePluginResult reactComponent(final String name, final Map<String, Object> props) {
        DocElement customComponent = new DocElement(DocElementType.CUSTOM_COMPONENT);
        customComponent.addProp("componentName", name);
        customComponent.addProp("componentProps", props);

        return new IncludePluginResult(customComponent);
    }

    public DocElement getDocElement() {
        return docElement;
    }
}
