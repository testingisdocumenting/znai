package com.twosigma.znai.java.parser;

public class JavaType {
    private String name;
    private String fullBody;
    private String bodyOnly;

    public JavaType(String name, String fullBody, String bodyOnly) {
        this.name = name;
        this.fullBody = fullBody;
        this.bodyOnly = bodyOnly;
    }

    public String getName() {
        return name;
    }

    public String getFullBody() {
        return fullBody;
    }

    public String getBodyOnly() {
        return bodyOnly;
    }
}
