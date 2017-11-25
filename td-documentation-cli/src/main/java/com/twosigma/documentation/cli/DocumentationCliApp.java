package com.twosigma.documentation.cli;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.AnsiConsoleOutput;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.parser.MarkupTypes;
import com.twosigma.documentation.website.WebSite;
import com.twosigma.documentation.client.DocumentationUploadClient;
import com.twosigma.documentation.html.WebResource;
import com.twosigma.documentation.server.DocumentationServer;
import com.twosigma.documentation.server.preview.DocumentationPreview;
import io.vertx.core.http.HttpServer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class DocumentationCliApp {
    private DocumentationCliConfig config;
    private Path deployPath;
    private WebSite webSite;

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

        if (! config.isServe()) {
            generateDocs();
        }

        if (config.isPreview()) {
            preview();
        } else if (config.isUpload()) {
            upload();
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
        HttpServer server = DocumentationServer.create(config.getDeployRoot());
        server.listen(config.getPort());
    }

    private void generateDocs() {
        webSite = WebSite.withToc(getTocPath()).
                withId(getDocId()).
                withMarkupType(config.getMarkupType()).
                withMetaFromJsonFile(config.getSourceRoot().resolve("meta.json")).
                withFileWithLookupPaths("lookup-paths").
                withFooterPath(config.getSourceRoot().resolve("footer.md")).
                withExtensionsDefPath(config.getSourceRoot().resolve("extensions.json")).
                withWebResources(WebResource.fromResource("static/favicon.png")).
                withEnabledPreview(config.isPreview()).deployTo(deployPath);
    }

    private Path getTocPath() {
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
