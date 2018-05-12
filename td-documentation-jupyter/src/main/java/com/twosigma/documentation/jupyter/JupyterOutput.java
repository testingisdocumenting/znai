package com.twosigma.documentation.jupyter;

public class JupyterOutput {
    public static final String HTML_FORMAT = "html";
    public static final String TEXT_FORMAT = "text";

    private final String type;
    private final String format;
    private final String content;

    public JupyterOutput(String type, String format, String content) {
        this.type = type;
        this.format = format;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    public String getContent() {
        return content;
    }
}
