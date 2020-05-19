/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.server;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZnaiServerConfig {
    private final String serverTitle;
    private final String serverType;

    private final Path deployRoot;
    private final Path docStorageRoot;

    public ZnaiServerConfig(Path deployRoot) {
        this.serverTitle = propertyOrDefault("znaiTitle", "Company");
        this.serverType = propertyOrDefault("znaiType", "Guides");
        this.deployRoot = deployRoot;

        this.docStorageRoot = buildDocStoragePath();
    }

    public String getServerTitle() {
        return serverTitle;
    }

    public String getServerType() {
        return serverType;
    }

    public Path getDeployRoot() {
        return deployRoot;
    }

    public Path getDocStorageRoot() {
        return docStorageRoot;
    }

    private Path buildDocStoragePath() {
        String docStoragePath = propertyOrDefault("docStoragePath", "");
        return docStoragePath.isEmpty() ? null : Paths.get(docStoragePath);
    }

    private static String propertyOrDefault(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }
}
