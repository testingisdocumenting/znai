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

package org.testingisdocumenting.znai.structure;

import java.nio.file.Path;

public class GlobalAnchor {
    private final String id;
    private final Path filePath;
    private final String url;

    public GlobalAnchor(String id, Path filePath, String url) {
        this.id = id;
        this.filePath = filePath;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public Path getFilePath() {
        return filePath;
    }

    public String getUrl() {
        return url;
    }
}
