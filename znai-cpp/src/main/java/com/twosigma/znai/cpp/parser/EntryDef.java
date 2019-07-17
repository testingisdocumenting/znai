package com.twosigma.znai.cpp.parser;

public class EntryDef {
    private String name;
    private String full;
    private String bodyOnly;

    public EntryDef(String name, String full, String bodyOnly) {
        this.name = name;
        this.full = full;
        this.bodyOnly = bodyOnly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull() {
        return full;
    }

    public void setBodyWithDecl(String bodyWithDecl) {
        this.full = bodyWithDecl;
    }

    public String getBodyOnly() {
        return bodyOnly;
    }

    public void setBodyOnly(String bodyOnly) {
        this.bodyOnly = bodyOnly;
    }

    @Override
    public String toString() {
        return "EntryDef{" +
                "name='" + name + '\'' +
                ", full='" + full + '\'' +
                ", bodyOnly='" + bodyOnly + '\'' +
                '}';
    }
}
