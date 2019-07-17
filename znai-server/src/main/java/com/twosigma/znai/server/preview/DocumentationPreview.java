package com.twosigma.znai.server.preview;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.znai.server.DocumentationServer;
import com.twosigma.znai.website.WebSite;
import io.vertx.core.http.HttpServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

public class DocumentationPreview {
    private final Path sourceRoot;
    private final Path deployRoot;

    public DocumentationPreview(Path sourceRoot, Path deployRoot) {
        this.sourceRoot = sourceRoot;
        this.deployRoot = deployRoot;
    }

    public void start(WebSite webSite, int port) {
        DocumentationServer documentationServer = new DocumentationServer(webSite.getReactJsBundle(), deployRoot);
        PreviewWebSocketHandler previewWebSocketHandler = new PreviewWebSocketHandler();
        documentationServer.addSocketHandler(previewWebSocketHandler);

        HttpServer server = documentationServer.create();

        reportPhase("starting server");
        final PreviewPushFileChangeHandler fileChangeHandler = new PreviewPushFileChangeHandler(
                previewWebSocketHandler,
                webSite);
        server.listen(port);

        reportPhase("initializing file watcher");
        final FileWatcher fileWatcher = new FileWatcher(sourceRoot,
                webSite.getAuxiliaryFilesRegistry().getAllPaths(),
                fileChangeHandler);

        reportHost(port);

        webSite.getAuxiliaryFilesRegistry().registerListener(fileWatcher);
        fileWatcher.start();
    }

    private void reportHost(int port) {
        try {
            ConsoleOutputs.out("http://", InetAddress.getLocalHost().getHostName(), ":", port, "/preview");
        } catch (UnknownHostException e) {
            // ignore
        }
    }

    private void reportPhase(String phase) {
        ConsoleOutputs.out(Color.BLUE, phase);
    }
}
