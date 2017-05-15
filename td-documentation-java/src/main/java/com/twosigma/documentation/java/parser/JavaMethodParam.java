package com.twosigma.documentation.java.parser;

/**
 * @author mykola
 */
public class JavaMethodParam {
    private String name;
    private String javaDocText;

    /**
     * @param name parameter name
     * @param javaDocText parameter javadoc <i>text</i> as is
     */
    public JavaMethodParam(String name, String javaDocText) {
        this.name = name;
        this.javaDocText = javaDocText;
    }

    public String getName() {
        return name;
    }

    public String getJavaDocText() {
        return javaDocText;
    }
}
