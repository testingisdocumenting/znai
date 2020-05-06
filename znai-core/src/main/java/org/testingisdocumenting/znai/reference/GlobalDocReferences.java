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

package org.testingisdocumenting.znai.reference;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.structure.DocUrl;
import org.testingisdocumenting.znai.utils.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class GlobalDocReferences {
    private ComponentsRegistry componentsRegistry;
    private final Path globalReferencesPath;
    private DocReferences docReferences;

    public GlobalDocReferences(ComponentsRegistry componentsRegistry, Path globalReferencesPath) {
        this.componentsRegistry = componentsRegistry;
        this.globalReferencesPath = globalReferencesPath;
    }

    public boolean isPresent() {
        return Files.exists(globalReferencesPath);
    }

    public void load() {
        docReferences = isPresent() ?
                DocReferencesParser.parse(FileUtils.fileTextContent(globalReferencesPath)):
                new DocReferences();

        validateLinks();
    }

    private void validateLinks() {
        DocStructure docStructure = componentsRegistry.docStructure();

        docReferences.pageUrlsStream().forEach(pageUrl ->
                docStructure.validateUrl(globalReferencesPath, "", new DocUrl(pageUrl)));
    }

    public Path getGlobalReferencesPath() {
        return globalReferencesPath;
    }

    public DocReferences getDocReferences() {
        return docReferences;
    }
}
