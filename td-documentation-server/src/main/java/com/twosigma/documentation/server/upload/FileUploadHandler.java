package com.twosigma.documentation.server.upload;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.streams.Pump;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class FileUploadHandler implements Handler<HttpServerRequest> {
    private Vertx vertx;
    private OnFileUploadHandler fileUploadHandler;
    private Path destination;

    public FileUploadHandler(Vertx vertx, String docId, OnFileUploadHandler fileUploadHandler) {
        this.vertx = vertx;
        this.destination = Paths.get(docId + ".zip");
        this.fileUploadHandler = fileUploadHandler;
    }

    @Override
    public void handle(HttpServerRequest req) {
        req.pause();
        vertx.fileSystem().open(destination.toString(), new OpenOptions(), fh -> {
            AsyncFile file = fh.result();
            Pump pump = Pump.pump(req, file);
            req.endHandler(eh -> file.close(fch -> {
                System.out.println("uploaded");
                req.response().end();
                fileUploadHandler.onUploadFinished(destination);
            }));

            pump.start();
            req.resume();
        });
    }
}
