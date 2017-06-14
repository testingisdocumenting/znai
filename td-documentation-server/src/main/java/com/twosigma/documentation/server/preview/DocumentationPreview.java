package com.twosigma.documentation.server.preview;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.WebSite;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.server.DocumentationServer;
import io.vertx.core.http.HttpServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

/**
 * @author mykola
 */
public class DocumentationPreview {
    private final Path sourceRoot;
    private final Path deployRoot;

    public DocumentationPreview(Path sourceRoot, Path deployRoot) {
        this.sourceRoot = sourceRoot;
        this.deployRoot = deployRoot;
    }

    public void start(WebSite webSite, int port) {
        HttpServer server = DocumentationServer.create(deployRoot);
        PreviewWebSocketHandler socketHandler = new PreviewWebSocketHandler();
        server.websocketHandler(socketHandler);

        reportPhase("starting server");
        final PreviewPushFileChangeHandler fileChangeHandler = new PreviewPushFileChangeHandler(socketHandler, webSite);
        server.listen(port);

        reportHost(port);

        reportPhase("initializing file watcher");
        final FileWatcher fileWatcher = new FileWatcher(sourceRoot,
                webSite.getAuxiliaryFiles().stream().map(AuxiliaryFile::getPath),
                fileChangeHandler);
        webSite.setAuxiliaryFileListener(fileWatcher);
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
