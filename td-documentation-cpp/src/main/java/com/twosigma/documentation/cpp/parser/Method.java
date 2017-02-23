package com.twosigma.documentation.cpp.parser;

/**
 * @author mykola
 */
public class Method {
    private String name;
    private String bodyWithDecl;
    private String bodyOnly;

    public Method(String name, String bodyWithDecl, String bodyOnly) {
        this.name = name;
        this.bodyWithDecl = bodyWithDecl;
        this.bodyOnly = bodyOnly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBodyWithDecl() {
        return bodyWithDecl;
    }

    public void setBodyWithDecl(String bodyWithDecl) {
        this.bodyWithDecl = bodyWithDecl;
    }

    public String getBodyOnly() {
        return bodyOnly;
    }

    public void setBodyOnly(String bodyOnly) {
        this.bodyOnly = bodyOnly;
    }

    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", bodyWithDecl='" + bodyWithDecl + '\'' +
                ", bodyOnly='" + bodyOnly + '\'' +
                '}';
    }
}
