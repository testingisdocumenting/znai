package com.twosigma.znai.client.testapp;

import com.twosigma.znai.client.DocumentationUploadClient;

import java.nio.file.Paths;

public class DocumentationUploadTestApp {
    public static void main(String[] args) {
        DocumentationUploadClient client = new DocumentationUploadClient("example-docs",
                Paths.get("znai-client/docs-build"), "localhost", 3333);

        client.upload((statusCode) -> System.out.println("uploaded with statusCode: " + statusCode));
    }
}
