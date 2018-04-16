package com.twosigma.documentation.cli;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.AnsiConsoleOutput;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.html.HtmlPage;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.parser.MarkupTypes;
import com.twosigma.documentation.website.WebSite;
import com.twosigma.documentation.client.DocumentationUploadClient;
import com.twosigma.documentation.html.WebResource;
import com.twosigma.documentation.server.DocumentationServer;
import com.twosigma.documentation.server.preview.DocumentationPreview;
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

    public DocumentationCliApp(String[] args) {
        System.setProperty("java.awt.headless", "true");
        this.config = new DocumentationCliConfig(args);
        this.deployPath = config.getDeployRoot().resolve(getDocId());
    }

    public static void main(String[] args) {
        DocumentationCliApp cliApp = new DocumentationCliApp(args);
        cliApp.start();
    }

    private String getDocId() {
        if (config.isPreview()) {
            return "preview";
        }

        return config.getDocId();
    }

    private void start() {
        ConsoleOutputs.add(new AnsiConsoleOutput());
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
        }
    }

    private void upload() {
        DocumentationUploadClient client = new DocumentationUploadClient(config.getDocId(), config.getDeployRoot(),
                config.getHost(), config.getPort());

        client.upload((statusCode -> System.exit(statusCode == 200 ? 0 : 1)));
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
                WebResource.withPath(userDefinedFavicon, HtmlPage.FAVICON_PATH) :
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
