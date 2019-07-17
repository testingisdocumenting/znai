package com.twosigma.znai.website;

import com.twosigma.znai.web.WebResource;
import com.twosigma.znai.web.extensions.WebSiteResourcesProvider;
import com.twosigma.utils.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.stream.Stream;

public class WebSiteLogoExtension implements WebSiteResourcesProvider {
    private WebResource cssLogoResource;

    WebSiteLogoExtension(Path docRootPath) {
        Path logoPath = docRootPath.resolve("logo.png");
        this.cssLogoResource = Files.exists(logoPath) ?
                WebResource.withTextContent("logo.css", createLogoCss(logoPath)):
                null;
    }

    @Override
    public Stream<WebResource> cssResources() {
        return cssLogoResource != null ? Stream.of(cssLogoResource) : Stream.empty();
    }

    private String createLogoCss(Path logoPath) {
        return ".mdoc-documentation-logo {\n" +
                "    display: block;\n" +
                "    background-size: contain;\n" +
                "    background-image: url(data:image/png;base64," + imageToBase64(logoPath) + ")\n" +
                "}\n";
    }

    private String imageToBase64(Path logoPath) {
        byte[] content = FileUtils.fileBinaryContent(logoPath);
        return Base64.getEncoder().encodeToString(content);
    }
}
