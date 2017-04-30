package com.twosigma.documentation.java.parser;

/**
 * @author mykola
 */
public class JavaMethodDetails {
    private String fullBody;
    private String bodyOnly;
    private String javaDocText;

    public JavaMethodDetails(String fullBody, String bodyOnly, String javaDocText) {
        this.fullBody = fullBody;
        this.bodyOnly = bodyOnly;
        this.javaDocText = javaDocText;
    }

    public String getFullBody() {
        return fullBody;
    }

    public String getBodyOnly() {
        return bodyOnly;
    }

    public String getJavaDocText() {
        return javaDocText;
    }
}
