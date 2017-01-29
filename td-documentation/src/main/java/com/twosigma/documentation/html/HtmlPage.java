package com.twosigma.documentation.html;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * @author mykola
 */
public class HtmlPage {
    private String title;

    private List<WebResource> cssResources;
    private List<WebResource> javaScriptResources;

    private List<RenderSupplier> headerSuppliers;
    private List<RenderSupplier> bodySuppliers;
    private List<RenderSupplier> javaScriptSuppliers;

    public HtmlPage() {
        title = "";

        cssResources = new ArrayList<>();
        javaScriptResources = new ArrayList<>();

        headerSuppliers = new ArrayList<>();
        bodySuppliers = new ArrayList<>();
        javaScriptSuppliers = new ArrayList<>();
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void addCss(WebResource webResource) {
        cssResources.add(webResource);
    }

    public void addJavaScript(WebResource webResource) {
        javaScriptResources.add(webResource);
    }

    public void addJavaScriptInFront(WebResource webResource) {
        javaScriptResources.add(0, webResource);
    }

    public void addToBody(RenderSupplier supplier) {
        bodySuppliers.add(supplier);
    }

    public void addToJavaScript(RenderSupplier supplier) {
        javaScriptSuppliers.add(supplier);
    }

    public String render(HtmlRenderContext renderContext) {
        return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<title>" + title + "</title>" +
            headerSuppliers.stream().map(s -> s.render(renderContext)).collect(joining("\n")) +
            cssResources.stream().map(r -> r.generateCssLink(renderContext)).collect(joining("\n")) +
            "\n</head>\n" +
            "<body>\n" +
            bodySuppliers.stream().map(s -> s.render(renderContext)).collect(joining("\n")) + "\n" +
            javaScriptResources.stream().map(r -> r.generateJavaScriptLink(renderContext)).collect(joining("\n")) + "\n" +
            "<script>\n" +
            javaScriptSuppliers.stream().map(s -> s.render(renderContext)).collect(joining("\n")) + "\n" +
            "</script>\n" +
            "\n</body>" +
            "\n</html>\n";
    }
}
