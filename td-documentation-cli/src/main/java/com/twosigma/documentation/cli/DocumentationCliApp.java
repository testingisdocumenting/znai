package com.twosigma.documentation.cli;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.AnsiConsoleOutput;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.cli.extension.CliCommandConfig;
import com.twosigma.documentation.html.HtmlPage;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.parser.MarkupTypes;
import com.twosigma.documentation.server.DocumentationServer;
import com.twosigma.documentation.server.preview.DocumentationPreview;
import com.twosigma.documentation.web.WebResource;
import com.twosigma.documentation.website.WebSite;
import io.vertx.core.http.HttpServer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class DocumentationCliApp {
    private DocumentationCliConfig config;
    private Path deployPath;
    private WebSite webSite;
    private ReactJsNashornEngine nashornEngine;

    public DocumentationCliApp(DocumentationCliConfig cliConfig) {
        System.setProperty("java.awt.headless", "true");
        this.config = cliConfig;
        this.deployPath = config.getDeployRoot().resolve(getDocId());
    }

    public static void start(DocumentationCliConfig cliConfig) {
        DocumentationCliApp cliApp = new DocumentationCliApp(cliConfig);
        cliApp.start();
    }

    public static void main(String[] args) {
        ConsoleOutputs.add(new AnsiConsoleOutput());
        start(new DocumentationCliConfig(args));
    }

    private String getDocId() {
        if (config.isPreview()) {
            return "preview";
        }

        return config.getDocId();
    }

    private void start() {
        config.print();

        if (config.isNew()) {
            createNew();
            return;
        }

        announceMode(config.getModeAsString());

        nashornEngine = new ReactJsNashornEngine();

        if (! config.isServe()) {
            generateDocs();
        }

        if (config.isPreview()) {
            preview();
        } else if (config.isServe()) {
            serve();
        } else if (config.isCustom()) {
            config.getSpecifiedCustomCommand().handle(
                    new CliCommandConfig(config.getDocId(), config.getSourceRoot(), config.getDeployRoot()));
        }
    }

    private void preview() {
        DocumentationPreview preview = new DocumentationPreview(config.getSourceRoot(), config.getDeployRoot());
        preview.start(webSite, config.getPort());
    }

    private void serve() {
        HttpServer server = new DocumentationServer(nashornEngine, config.getDeployRoot()).create();
        server.listen(config.getPort());
    }

    private void generateDocs() {
        Path userDefinedFavicon = config.getSourceRoot().resolve("favicon.png");
        WebResource favIconResource = Files.exists(userDefinedFavicon) ?
                WebResource.withPath(userDefinedFavicon, HtmlPage.FAVICON_PATH):
                WebResource.fromResource(HtmlPage.FAVICON_PATH);

        webSite = WebSite.withToc(resolveTocPath()).
                withReactJsNashornEngine(nashornEngine).
                withId(getDocId()).
                withMarkupType(config.getMarkupType()).
                withMetaFromJsonFile(config.getSourceRoot().resolve("meta.json")).
                withFileWithLookupPaths("lookup-paths").
                withFooterPath(config.getSourceRoot().resolve("footer.md")).
                withExtensionsDefPath(config.getSourceRoot().resolve("extensions.json")).
                withWebResources(favIconResource).
                withEnabledPreview(config.isPreview()).deployTo(deployPath);
    }

    private Path resolveTocPath() {
        switch (config.getMarkupType()) {
            case MarkupTypes.SPHINX:
                return config.getSourceRoot().resolve("index.xml");
            default:
                return config.getSourceRoot().resolve("toc");
        }
    }

    private void createNew() {
        ConsoleOutputs.out(Color.BLUE, "scaffolding new documentation");
        DocScaffolding scaffolding = new DocScaffolding(Paths.get("mdoc"));
        scaffolding.create();
    }

    private void announceMode(String name) {
        ConsoleOutputs.out(Color.BLUE, "mdoc ", Color.YELLOW, name + " mode");
    }
}
