package com.twosigma.documentation.java.parser;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class JavaMethod {
    private String name;
    private String fullBody;
    private String bodyOnly;
    private String javaDocText;
    private List<JavaMethodParam> params;

    public JavaMethod(String name, String fullBody, String bodyOnly, List<JavaMethodParam> params, String javaDocText) {
        this.name = name;
        this.fullBody = fullBody;
        this.bodyOnly = bodyOnly;
        this.javaDocText = javaDocText;
        this.params = params;
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

    public String getJavaDocText() {
        return javaDocText;
    }

    public List<JavaMethodParam> getParams() {
        return params;
    }

    public List<String> getParamNames() {
        return params.stream().map(JavaMethodParam::getName).collect(toList());
    }
}
