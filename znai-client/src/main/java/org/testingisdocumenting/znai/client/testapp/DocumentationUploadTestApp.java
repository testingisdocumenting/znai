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
