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

package com.twosigma.znai.reference;

import com.twosigma.znai.utils.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class GlobalDocReferences {
    private final Path globalReferencesPath;
    private DocReferences docReferences;

    public GlobalDocReferences(Path globalReferencesPath) {
        this.globalReferencesPath = globalReferencesPath;
        this.reload();
    }

    public boolean isPresent() {
        return Files.exists(globalReferencesPath);
    }

    public void reload() {
        docReferences = isPresent() ?
                DocReferencesParser.parse(FileUtils.fileTextContent(globalReferencesPath)):
                new DocReferences(Collections.emptyMap());
    }

    public DocReferences getDocReferences() {
        return docReferences;
    }
}
