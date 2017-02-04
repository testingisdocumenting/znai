package com.twosigma.documentation.server;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.AnsiConsoleOutput;
import io.vertx.core.http.HttpServer;

/**
 * @author mykola
 */
public class DocumentationServerApp {
    public static void main(String[] args) {
        ConsoleOutputs.add(new AnsiConsoleOutput());

        ServerConfig serverConfig = new ServerConfig(args);
        HttpServer server = DocumentationServer.create(serverConfig);
        server.listen(8080);
    }
}
