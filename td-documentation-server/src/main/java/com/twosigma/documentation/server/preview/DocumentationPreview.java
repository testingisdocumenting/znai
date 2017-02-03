package com.twosigma.documentation.server.preview;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.AnsiConsoleOutput;
import com.twosigma.documentation.WebSite;
import com.twosigma.documentation.server.DocumentationServer;
import com.twosigma.documentation.server.ServerConfig;
import io.vertx.core.http.HttpServer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class DocumentationPreview {
    private ServerConfig serverConfig;

    public DocumentationPreview(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void start() {
        final Path previewPath = serverConfig.getDeployRoot();

        HttpServer server = DocumentationServer.create(serverConfig);
        PreviewWebSocketHandler socketHandler = new PreviewWebSocketHandler();
        server.websocketHandler(socketHandler);

        Path docRoot = serverConfig.getDocRoot();
        Path tocPath = docRoot.resolve("toc");

        final WebSite webSite = WebSite.withToc(tocPath).
            withMetaFromJsonFile(docRoot.resolve("meta.json")).
                withEnabledPreview().
                deployTo(previewPath.resolve("preview"));

        final PreviewPushFileChangeHandler fileChangeHandler = new PreviewPushFileChangeHandler(socketHandler, webSite);

        server.listen(8080);

        final FileWatcher fileWatcher = new FileWatcher(docRoot, fileChangeHandler);
        fileWatcher.start();
    }
}
