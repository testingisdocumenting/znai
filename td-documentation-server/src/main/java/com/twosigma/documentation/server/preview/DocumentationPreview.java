package com.twosigma.documentation.server.preview;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.WebSite;
import com.twosigma.documentation.server.DocumentationServer;
import com.twosigma.documentation.server.ServerConfig;
import io.vertx.core.http.HttpServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

/**
 * @author mykola
 */
public class DocumentationPreview {
    private ServerConfig serverConfig;

    public DocumentationPreview(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void start() {
        serverConfig.print();

        final Path previewPath = serverConfig.getDeployRoot();

        HttpServer server = DocumentationServer.create(serverConfig);
        PreviewWebSocketHandler socketHandler = new PreviewWebSocketHandler();
        server.websocketHandler(socketHandler);

        Path docRoot = serverConfig.getSourceRoot();
        Path tocPath = docRoot.resolve("toc");

        final WebSite webSite = WebSite.withToc(tocPath).
            withMetaFromJsonFile(docRoot.resolve("meta.json")).
                withEnabledPreview().
                deployTo(previewPath.resolve("preview"));

        reportPhase("starting server");
        final PreviewPushFileChangeHandler fileChangeHandler = new PreviewPushFileChangeHandler(socketHandler, webSite);
        server.listen(serverConfig.getPort());

        reportHost();

        reportPhase("initializing file watcher");
        final FileWatcher fileWatcher = new FileWatcher(docRoot, fileChangeHandler);
        fileWatcher.start();
    }

    private void reportHost() {
        try {
            ConsoleOutputs.out(InetAddress.getLocalHost().getHostName(), ":", serverConfig.getPort());
        } catch (UnknownHostException e) {
            // ignore
        }
    }

    private void reportPhase(String phase) {
        ConsoleOutputs.out(Color.BLUE, phase);
    }
}
