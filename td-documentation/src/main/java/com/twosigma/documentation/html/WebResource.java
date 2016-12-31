package com.twosigma.documentation.html;

import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.twosigma.utils.FileUtils;
import com.twosigma.utils.ResourceUtils;

/**
 * @author mykola
 */
public class WebResource {
    private Path originPath;
    private String resourceContent;
    private String relativePath;

    private WebResource(final Path originPath, final String relativePath) {
        this.originPath = originPath;
        this.relativePath = relativePath;
    }

    private WebResource(final String resourcePath) {
        this.relativePath = resourcePath;
        this.resourceContent = ResourceUtils.textContent(resourcePath);
    }

    public static WebResource withRelativePath(final String relativePath) {
        return new WebResource(null, relativePath);
    }

    public static WebResource fromResource(final String resourcePath) {
        return new WebResource(resourcePath);
    }

    public static WebResource fromFileSystemWithRelativePath(final Path originPath, final String relativePath) {
        return new WebResource(originPath, relativePath);
    }

    public Path getOriginPath() {
        return originPath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String generateCssLink(HtmlRenderContext renderContext) {
        return "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + nestedPath(renderContext) + "\">";
    }

    public String generateJavaScriptLink(HtmlRenderContext renderContext) {
        return "<script type=\"text/javascript\" src=\"" + nestedPath(renderContext) + "\"></script>";
    }

    public String generateImageLink(HtmlRenderContext renderContext, final int pixelWidth) {
        return "<img src=\"" + nestedPath(renderContext) + "\" width=\"" + pixelWidth + "px\">";
    }

    private String nestedPath(HtmlRenderContext renderContext) {
        return IntStream.range(0, renderContext.getNestLevel()).mapToObj(l -> "..").collect(Collectors.joining("/")) +
            (renderContext.getNestLevel() == 0 ? "" : "/") + relativePath;
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
                ", relativePath='" + relativePath + '\'' +
                '}';
    }
}
