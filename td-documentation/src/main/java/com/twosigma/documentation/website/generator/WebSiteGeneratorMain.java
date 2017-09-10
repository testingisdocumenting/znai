package com.twosigma.documentation.website.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.AnsiConsoleOutput;
import com.twosigma.documentation.WebSite;

/**
 * @author mykola
 */
public class WebSiteGeneratorMain {
    public static void main(String[] args) {
        ConsoleOutputs.add(new AnsiConsoleOutput());

        final Path deployPath = Paths.get("dist/documentation");
        final Path mdRoot = Paths.get("documentation");

        WebSite.withToc(mdRoot.resolve("toc")).
                withFooterPath(mdRoot.resolve("footer.md")).
                withMetaFromJsonFile(mdRoot.resolve("meta.json")).
                withLogoRelativePath("img/two-sigma-logo.png").
                deployTo(deployPath);
    }
}
