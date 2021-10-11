/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.client.upload;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testingisdocumenting.znai.client.ZipTask;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * zips and uploads znai produced docs to provided url
 * assumes the presence of meta.json file
 * if files are not zipped, they will be zipped
 * @see DocHubUploader
 */
public class DocUploader {
    private static final String USER = System.getProperty("USER", "");

    private final String uploadUrl;
    private final String docId;
    private final Path path;
    private final String actor;

    private DocUploader(String uploadUrl, String docId, Path path, String actor) {
        this.uploadUrl = uploadUrl;
        this.docId = docId;
        this.path = path;
        this.actor = actor;
    }

    public static void upload(String uploadUrl, String docId, Path path, String actor) {
        new DocUploader(uploadUrl, docId, path, getActor(actor)).upload();
    }

    public static void uploadZip(String uploadUrl, String docId, Path path, String actor) {
        new DocUploader(uploadUrl, docId, path, getActor(actor)).uploadZip();
    }

    private void upload() {
        UploadPathValidator.validateFile(path, "index.html");
        UploadPathValidator.validateSize(path);
        Path zipPath = zipDocs(path);
        httpPut(zipPath);
    }

    private void uploadZip() {
        UploadPathValidator.validateSize(path);
        httpPut(path);
    }

    private void httpPut(Path zipPath) {
        ConsoleOutputs.out(Color.BLUE, "uploading documentation: ", Color.PURPLE, zipPath,
                Color.BLUE, " as ", Color.PURPLE, buildAccessUrl());

        FileEntity entity = new FileEntity(zipPath.toFile(),
                ContentType.create("application/octet-stream", "UTF-8"));

        HttpPut put = new HttpPut(buildUploadUrl());
        put.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            CloseableHttpResponse httpResponse = httpClient.execute(put);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                ConsoleOutputs.err(statusLine.toString());
            } else {
                ConsoleOutputs.out(Color.BLUE, "upload finished");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildUploadUrl() {
        return uploadUrl + "/upload/" + docId + "?actor=" + actor;
    }

    private String buildAccessUrl() {
        return uploadUrl + "/" + docId;
    }

    private Path zipDocs(Path path) {
        ConsoleOutputs.out(Color.BLUE, "zipping: ", Color.PURPLE, path);

        Path zipDestination = Paths.get(docId + ".zip");
        zipDestination.toFile().deleteOnExit();

        ZipTask zipTask = new ZipTask(path, zipDestination);
        zipTask.execute();

        return zipDestination;
    }

    private static String getActor(String actor) {
        return StringUtils.isBlank(actor) ? USER : actor;
    }
}
