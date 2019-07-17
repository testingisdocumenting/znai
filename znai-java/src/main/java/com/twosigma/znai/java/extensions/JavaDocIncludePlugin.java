package com.twosigma.znai.java.extensions;

import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.java.parser.JavaCode;
import com.twosigma.znai.java.parser.html.HtmlToDocElementConverter;

public class JavaDocIncludePlugin extends JavaIncludePluginBase {
    @Override
    public String id() {
        return "java-doc";
    }

    @Override
    public IncludePlugin create() {
        return new JavaDocIncludePlugin();
    }

    @Override
    public JavaIncludeResult process(JavaCode javaCode) {
        String entry = pluginParams.getOpts().get("entry");

        return new JavaIncludeResult(HtmlToDocElementConverter.convert(componentsRegistry, markupPath, entry == null ?
                javaCode.getClassJavaDocText() :
                javaCode.findJavaDoc(entry)), entry);
    }
}
