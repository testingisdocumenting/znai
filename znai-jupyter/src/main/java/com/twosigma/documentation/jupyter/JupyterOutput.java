package com.twosigma.documentation.jupyter;

public class JupyterOutput {
    public static final String HTML_FORMAT = "html";
    public static final String SVG_FORMAT = "svg";
    public static final String IMG_FORMAT = "img";
    public static final String TEXT_FORMAT = "text";

    private final String format;
    private final String content;

    public JupyterOutput(String format, String content) {
        this.format = format;
        this.content = content;
    }

    public String getFormat() {
        return format;
    }

    public String getContent() {
        return content;
    }
}
