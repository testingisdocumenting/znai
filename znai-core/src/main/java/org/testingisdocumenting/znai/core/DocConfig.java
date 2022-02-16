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

package org.testingisdocumenting.znai.core;

import java.nio.file.Path;

public class DocConfig {
    private final Path sourceRoot;
    private final boolean isValidateExternalLinks;

    public DocConfig(Path sourceRoot, boolean validateExternalLinks) {
        this.sourceRoot = sourceRoot;
        this.isValidateExternalLinks = validateExternalLinks;
    }

    public Path getDocRoot() {
        return sourceRoot;
    }

    public boolean isValidateExternalLinks() {
        return isValidateExternalLinks;
    }
}
