package com.twosigma.documentation.java.parser;

public class JavaMethodParam {
    private String name;
    private String javaDocText;
    private String type;

    /**
     * @param name parameter name
     * @param javaDocText parameter javadoc <i>text</i> as is
     * @param type parameter type
     */
    public JavaMethodParam(String name, String javaDocText, String type) {
        this.name = name;
        this.javaDocText = javaDocText;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getJavaDocText() {
        return javaDocText;
    }

    public String getType() {
        return type;
    }
}
