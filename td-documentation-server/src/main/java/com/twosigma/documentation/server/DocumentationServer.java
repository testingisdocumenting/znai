package com.twosigma.documentation.server;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.server.upload.FileUploadHandler;
import com.twosigma.documentation.server.upload.UnzipTask;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author mykola
 */
public class DocumentationServer {
    public static HttpServer create(Path deployRoot) {
        createDirs(deployRoot);
        System.setProperty("vertx.cwd", deployRoot.toString());

        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        StaticHandler pagesStaticHandler = StaticHandler.create(".").
                setFilesReadOnly(false).setMaxAgeSeconds(0);

        StaticHandler staticCommonResources =
                StaticHandler.create("static").setMaxAgeSeconds(600);

        router.get("/static/*").handler(staticCommonResources);

        router.route("/upload/:docId").handler(ctx -> {
            MultiMap params = ctx.request().params();
            String docId = params.get("docId");
            new FileUploadHandler(vertx, deployRoot, docId, (p) -> unzip(deployRoot, docId, p)).handle(ctx.request());
        });

        router.get("/:product/:section/:page").handler(rc -> {
            MultiMap params = rc.request().params();
            System.out.println(params);

            rc.next();
        });

        router.get("/*").handler(pagesStaticHandler);


        server.requestHandler(router::accept);

        return server;
    }

    private static void createDirs(Path deployRoot) {
        try {
            Files.createDirectories(deployRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void unzip(Path root, String docId, Path path) {
        ConsoleOutputs.out(Color.BLUE, "unzipping docs: ", Color.PURPLE, path, Color.BLACK, " to ",
                Color.PURPLE, root);

        UnzipTask unzipTask = new UnzipTask(root.resolve(path), root);
        unzipTask.execute();
        ConsoleOutputs.out(Color.BLUE, "unzipped docs: ", Color.PURPLE, root);
    }
}
