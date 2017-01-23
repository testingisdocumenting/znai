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
    private String path;
    private boolean isPathRelative;

    private WebResource(final Path originPath, final String path, boolean isPathRelative) {
        this.originPath = originPath;
        this.path = path;
        this.isPathRelative = isPathRelative;
    }

    private WebResource(final String resourcePath) {
        this.path = resourcePath;
        this.isPathRelative = false;
        this.resourceContent = ResourceUtils.textContent(resourcePath);
    }

    public static WebResource withRelativePath(final String relativePath) {
        return new WebResource(null, relativePath, true);
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

    public String generateCssLink(HtmlRenderContext renderContext) {
        return "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + pathForHtml(renderContext) + "\">";
    }

    public String generateJavaScriptLink(HtmlRenderContext renderContext) {
        return "<script type=\"text/javascript\" src=\"" + pathForHtml(renderContext) + "\"></script>";
    }

    private String pathForHtml(HtmlRenderContext renderContext) {
        return isPathRelative ? nestedPath(renderContext) : toAbsolute(path);
    }

    private String toAbsolute(String path) {
        return path.startsWith("/") ? path : "/" + path;
    }

    private String nestedPath(HtmlRenderContext renderContext) {
        return IntStream.range(0, renderContext.getNestLevel()).mapToObj(l -> "..").collect(Collectors.joining("/")) +
            (renderContext.getNestLevel() == 0 ? "" : "/") + path;
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
