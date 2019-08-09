/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.znai.client;

import com.twosigma.znai.console.ConsoleOutputs;
import com.twosigma.znai.console.ansi.Color;
import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.streams.Pump;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentationUploadClient {
    private final Vertx vertx;
    private final String docId;
    private final Path deployRoot;
    private final String host;
    private final int port;

    public DocumentationUploadClient(String docId, Path deployRoot, String host, int port) {
        this.vertx = Vertx.vertx();
        this.docId = docId;
        this.deployRoot = deployRoot;
        this.host = host;
        this.port = port;
    }

    public void upload(OnUploadFinishedClientHandler onUploadFinish) {
        upload(false, onUploadFinish);
    }

    public void uploadWithSsl(OnUploadFinishedClientHandler onUploadFinish) {
        upload(true, onUploadFinish);
    }

    private void upload(boolean useSsl, OnUploadFinishedClientHandler onUploadFinish) {
        Path zipPath = zipDocs(deployRoot);
        upload(zipPath, useSsl, onUploadFinish);
    }

    private Path zipDocs(Path dirToZip) {
        ConsoleOutputs.out(Color.BLUE, "zipping: ", Color.PURPLE, dirToZip);

        Path zipDestination = Paths.get(docId + ".zip");
        zipDestination.toFile().deleteOnExit();
        ZipTask zipTask = new ZipTask(dirToZip, zipDestination);
        zipTask.execute();

        return zipDestination;
    }

    private void upload(Path path, boolean useSsl, OnUploadFinishedClientHandler onUploadFinish) {
        ConsoleOutputs.out(Color.BLUE, "uploading documentation: ", Color.GREEN,
                path, Color.BLACK, " to ", Color.PURPLE, fullUrl(docId));

        HttpClient client = vertx.createHttpClient(new HttpClientOptions().setSsl(useSsl));
        HttpClientRequest req = client.put(port, host,
                "/upload/" + docId, resp -> handleUploadFinish(resp.statusCode(), onUploadFinish));

        FileSystem fs = vertx.fileSystem();
        fs.props(path.toString(), ah -> {
            FileProps props = ah.result();

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

    private String fullUrl(String docId) {
        return host +
                (port != 80 && port != 443 ? ":" + port : "") +
                "/" + docId;
    }

    private void handleUploadFinish(int statusCode, OnUploadFinishedClientHandler onUploadFinish) {
        if (statusCode == 200) {
            ConsoleOutputs.out(Color.BLUE, "upload finished");
        } else {
            ConsoleOutputs.err("upload failed ", statusCode);
        }

        onUploadFinish.onUpload(statusCode);
        vertx.close();
    }
}
