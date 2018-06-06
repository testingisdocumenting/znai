package com.twosigma.documentation.web;

import com.twosigma.utils.FileUtils;
import com.twosigma.utils.ResourceUtils;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class WebResource {
    private final Path originPath;
    private final String path;
    private final byte[] resourceContent;

    private WebResource(final Path originPath, final String path) {
        this.originPath = originPath;
        this.path = path;
        this.resourceContent = null;
    }

    private WebResource(final String resourcePath) {
        this.originPath = null;
        this.path = resourcePath;
        this.resourceContent = ResourceUtils.binaryContent(resourcePath);
    }

    private WebResource(final String path, final byte[] content) {
        this.originPath = null;
        this.path = path;
        this.resourceContent = content;
    }

    public static WebResource withPath(final String path) {
        return new WebResource(null, path);
    }
    
    public static WebResource withPath(final Path originPath, final String webPath) {
        return new WebResource(originPath, webPath);
    }

    public static WebResource fromResource(final String resourcePath) {
        return new WebResource(resourcePath);
    }

    public static WebResource withTextContent(final String path, final String content) {
        return new WebResource(path, content.getBytes());
    }

    public Path getOriginPath() {
        return originPath;
    }

    public String getPath() {
        return path;
    }

    public String generateCssLink(String documentationId) {
        return "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + pathForHtml(documentationId) + "\">";
    }

    public String generateJavaScriptLink(String documentationId) {
        return "<script type=\"text/javascript\" src=\"" + pathForHtml(documentationId) + "\"></script>";
    }

    private String pathForHtml(String documentationId) {
        return "/" + ((documentationId.isEmpty()) ? "" : documentationId + "/") + path;
    }

    public byte[] getBinaryContent() {
        return resourceContent == null ?
                FileUtils.fileBinaryContent(originPath):
                resourceContent;
    }

    public String getTextContent() {
        return new String(getBinaryContent());
    }

    @Override
    public String toString() {
        return (resourceContent == null ? "FromDisk" : "FromResource") + "{" +
                "originPath=" + originPath +
                ", path='" + path + '\'' +
                '}';
    }
}
