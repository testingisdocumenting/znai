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

package com.twosigma.znai.server.upload;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.streams.Pump;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentationUploadHandler implements Handler<HttpServerRequest> {
    private Vertx vertx;
    private final String docId;
    private final Path destination;
    private final String actor;

    public DocumentationUploadHandler(Vertx vertx, String docId, Path deployRoot, String actor) {
        this.vertx = vertx;
        this.docId = docId;
        this.destination = deployRoot.resolve(docId + ".zip");
        this.actor = actor;
    }

    @Override
    public void handle(HttpServerRequest req) {
        req.pause();

        vertx.fileSystem().open(destination.toString(), new OpenOptions(), fh -> {
            AsyncFile file = fh.result();
            Pump pump = Pump.pump(req, file);
            req.endHandler(eh -> file.close(fch -> {
                req.response().end();

                OnUploadFinishedServerHandlers.onUploadFinished(docId, destination, actor);
                deleteUploadedFile();
            }));

            pump.start();
            req.resume();
        });
    }

    private void deleteUploadedFile()  {
        try {
            Files.delete(destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
