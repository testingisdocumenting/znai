package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.java.parser.JavaCode;
import com.twosigma.documentation.java.parser.html.HtmlToDocElementConverter;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.util.List;

/**
 * @author mykola
 */
public class JavaDocIncludePlugin extends JavaIncludePluginBase {
    @Override
    public String id() {
        return "java-doc";
    }

    @Override
    public List<DocElement> process(JavaCode javaCode) {
        String entry = pluginParams.getOpts().get("entry");

        return HtmlToDocElementConverter.convert(componentsRegistry, markupPath, entry == null ?
                javaCode.getClassJavaDocText() :
                javaCode.methodByName(entry).getJavaDocText());
    }
}
