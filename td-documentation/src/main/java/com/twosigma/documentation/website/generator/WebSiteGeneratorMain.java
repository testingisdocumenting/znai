package com.twosigma.documentation.website.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.twosigma.documentation.WebSite;

import static com.twosigma.documentation.html.reactjs.ReactWebResources.react;
import static com.twosigma.documentation.html.reactjs.ReactWebResources.reactDom;

/**
 * @author mykola
 */
public class WebSiteGeneratorMain {
    public static void main(String[] args) {
        final Path deployPath = Paths.get("dist/webdoc");
        final Path mdRoot = Paths.get("webdoc");

        WebSite.withToc(mdRoot.resolve("toc")).
            withMetaFromJsonFile(mdRoot.resolve("meta.json")).
            withLogoRelativePath("img/two-sigma-logo.png").
            deployTo(deployPath);
    }
}
