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
package org.testingisdocumenting.znai.enterprise.storage;

import org.testingisdocumenting.znai.server.ZnaiServerConfig;
import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.util.Objects;
import java.util.Set;

public class DocumentationStorageFactories {
    private static final Set<DocumentationStorageFactory> handlers =
            ServiceLoaderUtils.load(DocumentationStorageFactory.class);

    public static DocumentationStorage create(ZnaiServerConfig config) {
        return handlers.stream()
                .map(h -> h.create(config))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static void add(DocumentationStorageFactory handler) {
        handlers.add(handler);
    }
}
