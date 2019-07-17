package com.twosigma.znai.java.parser;

public class JavaMethodReturn {
    private String type;
    private String javaDocText;

    public JavaMethodReturn(String type, String javaDocText) {
        this.type = type;
        this.javaDocText = javaDocText;
    }

    public String getType() {
        return type;
    }

    public String getJavaDocText() {
        return javaDocText;
    }
}
