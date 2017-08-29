package com.twosigma.documentation.html;

import java.nio.file.Path;

import com.twosigma.utils.FileUtils;
import com.twosigma.utils.ResourceUtils;

/**
 * @author mykola
 */
public class WebResource {
    private Path originPath;
    private byte[] resourceContent;
    private String path;

    private WebResource(final Path originPath, final String path) {
        this.originPath = originPath;
        this.path = path;
    }

    private WebResource(final String resourcePath) {
        this.path = resourcePath;
        this.resourceContent = ResourceUtils.binaryContent(resourcePath);
    }

    public static WebResource withPath(final String path) {
        return new WebResource(null, path);
    }

    public static WebResource fromResource(final String resourcePath) {
        return new WebResource(resourcePath);
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
