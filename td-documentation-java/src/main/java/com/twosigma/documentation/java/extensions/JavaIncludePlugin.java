package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.codesnippets.CodeSnippetsProps;
import com.twosigma.documentation.java.parser.JavaCode;
import com.twosigma.documentation.java.parser.JavaMethod;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public class JavaIncludePlugin extends JavaIncludePluginBase {
    @Override
    public String id() {
        return "java";
    }

    @Override
    public List<DocElement> process(JavaCode javaCode) {
        Boolean bodyOnly = pluginParams.getOpts().has("bodyOnly") ? pluginParams.getOpts().get("bodyOnly") : false;

        Map<String, Object> props = CodeSnippetsProps.create(componentsRegistry.codeTokenizer(), "java",
                extractContent(javaCode, entry, bodyOnly));

        DocElement docElement = new DocElement(DocElementType.SNIPPET);
        props.forEach(docElement::addProp);

        return Collections.singletonList(docElement);
    }

    private static String extractContent(JavaCode javaCode, String methodName, Boolean bodyOnly) {
        if (methodName == null) {
            return javaCode.getFileContent();
        }

        JavaMethod method = javaCode.methodByName(methodName);

        return bodyOnly ?
                method.getBodyOnly() :
                method.getFullBody();
    }
}
