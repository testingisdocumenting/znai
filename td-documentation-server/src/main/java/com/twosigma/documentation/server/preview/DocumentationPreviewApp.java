package com.twosigma.documentation.server.preview;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.AnsiConsoleOutput;
import com.twosigma.documentation.WebSite;
import com.twosigma.documentation.server.DocumentationServer;
import com.twosigma.documentation.server.ServerConfig;
import io.vertx.core.http.HttpServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class DocumentationPreviewApp {
    public static void main(String[] args) throws IOException {
        ConsoleOutputs.add(new AnsiConsoleOutput());

        Path deployRoot = PreviewTempDir.get();
        deployRoot.toFile().deleteOnExit();

        ServerConfig serverConfig = new ServerConfig(args);
        serverConfig.setDeployRoot(deployRoot);

        DocumentationPreview preview = new DocumentationPreview(serverConfig);
        preview.start();
    }
}
