package com.twosigma.documentation.html;

import java.nio.file.Path;

import com.twosigma.utils.FileUtils;
import com.twosigma.utils.ResourceUtils;

/**
 * @author mykola
 */
public class WebResource {
    private Path originPath;
    private String resourceContent;
    private String path;

    private WebResource(final Path originPath, final String path) {
        this.originPath = originPath;
        this.path = path;
    }

    private WebResource(final String resourcePath) {
        this.path = resourcePath;
        this.resourceContent = ResourceUtils.textContent(resourcePath);
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
        // for resource based content we ignore documentation id when building full path
        //
        boolean isResourceBased = resourceContent != null;
        return "/" +
                ((documentationId.isEmpty() || isResourceBased) ?
                        "" : documentationId + "/") + path;
    }

    public String getContent() {
        return resourceContent == null ?
                FileUtils.fileTextContent(originPath):
                resourceContent;
    }

    @Override
    public String toString() {
        return (resourceContent == null ? "FromDisk" : "FromResource") + "{" +
                "originPath=" + originPath +
                ", path='" + path + '\'' +
                '}';
    }
}
