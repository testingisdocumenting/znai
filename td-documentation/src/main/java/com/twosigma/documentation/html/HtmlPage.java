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

    public String render(String documentationId) {
        return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\" /> \n" +
            "<title>" + title + "</title>" +
            headerSuppliers.stream().map(RenderSupplier::render).collect(joining("\n")) +
            cssResources.stream().map(r -> r.generateCssLink(documentationId)).collect(joining("\n")) +
            "\n</head>\n" +
            "<link rel=\"shortcut icon\" href=\"/" + documentationId + "/static/favicon.png\" type=\"image/ico\"/>\n" +
            "<body>\n" +
            bodySuppliers.stream().map(RenderSupplier::render).collect(joining("\n")) + "\n" +
            javaScriptResources.stream().map(r -> r.generateJavaScriptLink(documentationId)).collect(joining("\n")) + "\n" +
            "<script>\n" +
            javaScriptSuppliers.stream().map(RenderSupplier::render).collect(joining("\n")) + "\n" +
            "</script>\n" +
            "\n</body>" +
            "\n</html>\n";
    }
}
