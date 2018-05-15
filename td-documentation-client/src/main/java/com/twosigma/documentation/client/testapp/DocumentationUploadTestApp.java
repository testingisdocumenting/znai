package com.twosigma.documentation.client.testapp;

import com.twosigma.documentation.client.DocumentationUploadClient;

import java.nio.file.Paths;

public class DocumentationUploadTestApp {
    public static void main(String[] args) {
        DocumentationUploadClient client = new DocumentationUploadClient("example-docs",
                Paths.get("td-documentation-client/docs-build"), "localhost", 3333);

        client.upload((statusCode) -> System.out.println("uploaded with statusCode: " + statusCode));
    }
}
