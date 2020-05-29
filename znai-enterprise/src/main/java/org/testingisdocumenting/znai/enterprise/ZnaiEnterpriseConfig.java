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

package org.testingisdocumenting.znai.enterprise;

import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class ZnaiEnterpriseConfig {
    private static final String CONFIG_FILE_NAME = "znai-enterprise.cfg";

    private final Path fsMonitorConfigPath;
    private final Path docStorageRoot;
    private final Map<String, Object> jsonConfig;

    public ZnaiEnterpriseConfig() {
        jsonConfig = getJsonConfig();

        fsMonitorConfigPath = buildFsMonitorConfigPath();
        docStorageRoot = buildDocStorageRoot();
    }

    public Path getFsMonitorConfigPath() {
        return fsMonitorConfigPath;
    }

    public Path getDocStorageRoot() {
        return docStorageRoot;
    }

    private Path buildFsMonitorConfigPath() {
        return Paths.get(configValueOrDefault("znaiFsMonitorConfig", "monitor.config.json"));
    }

    private Path buildDocStorageRoot() {
        String docStoragePath = configValueOrDefault("znaiDocStoragePath", "");
        return docStoragePath.isEmpty() ? null : Paths.get(docStoragePath);
    }

    private String configValueOrDefault(String key, String defaultValue) {
        return systemPropertyOrDefault(key, jsonConfig.getOrDefault(key, defaultValue).toString());
    }

    private static String systemPropertyOrDefault(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getJsonConfig() {
        Path configPath = Paths.get(CONFIG_FILE_NAME);

        return Files.exists(configPath) ? (Map < String, Object >) JsonUtils.deserializeAsMap(
                FileUtils.fileTextContent(configPath)): Collections.emptyMap();
    }
}
