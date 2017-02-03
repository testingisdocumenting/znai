package com.twosigma.documentation.client;

import io.vertx.core.*;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.streams.Pump;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class DocumentationUploader {
    private UploadFinishedHandler onUploadFinish;

    public DocumentationUploader(UploadFinishedHandler onUploadFinish) {
        this.onUploadFinish = onUploadFinish;
    }

    public void upload(String docId, Path path) {
        Vertx vertx = Vertx.vertx();
        HttpClientRequest req = vertx.createHttpClient(new HttpClientOptions()).put(8080, "localhost",
                "/upload/" + docId, resp -> {
            onUploadFinish.onUpload(resp.statusCode());
        });

        FileSystem fs = vertx.fileSystem();
        fs.props(path.toString(), ah -> {
            FileProps props = ah.result();
            System.out.println(props);

            long size = props.size();

            req.headers().set("content-length", String.valueOf(size));
            fs.open(path.toString(), new OpenOptions(), oh -> {
                AsyncFile file = oh.result();
                Pump pump = Pump.pump(file, req);
                file.endHandler(v -> req.end());
                pump.start();
            });
        });
    }
}
