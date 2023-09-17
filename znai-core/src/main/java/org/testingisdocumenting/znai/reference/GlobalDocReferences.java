/*
 * Copyright 2021 znai maintainers
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
    private final ComponentsRegistry componentsRegistry;

    private final Path globalReferencesPathNoExt;
    private final Path globalReferencesPathCsv;
    private final Path globalReferencesPathJson;

    private DocReferences docReferences;

    public GlobalDocReferences(ComponentsRegistry componentsRegistry, Path globalReferencesPathNoExt) {
        this.componentsRegistry = componentsRegistry;
        this.globalReferencesPathNoExt = globalReferencesPathNoExt;
        this.globalReferencesPathCsv = globalReferencesPathNoExt.resolveSibling(globalReferencesPathNoExt.getFileName() + ".csv");
        this.globalReferencesPathJson = globalReferencesPathNoExt.resolveSibling(globalReferencesPathNoExt.getFileName() + ".json");
    }

    public boolean isPresent() {
        return isCsvPresent() || isJsonPresent();
    }

    public boolean isCsvPresent() {
        return Files.exists(globalReferencesPathCsv);
    }

    public boolean isJsonPresent() {
        return Files.exists(globalReferencesPathJson);
    }

    public void load() {
        String content = readReferenceContent();
        docReferences = content.isEmpty() ?
                new DocReferences() :
                DocReferencesParser.parse(content);

        validateLinks();
    }

    private void validateLinks() {
        DocStructure docStructure = componentsRegistry.docStructure();

        docReferences.pageUrlsStream().forEach(pageUrl ->
                docStructure.validateUrl(globalReferencesPathNoExt, "", new DocUrl(globalReferencesPathNoExt, pageUrl)));
    }

    public Path getGlobalReferencesPathNoExt() {
        return globalReferencesPathNoExt;
    }

    public DocReferences getDocReferences() {
        return docReferences;
    }

    private String readReferenceContent() {
        if (isCsvPresent()) {
            return FileUtils.fileTextContent(globalReferencesPathCsv);
        }

        if (isJsonPresent()) {
            return FileUtils.fileTextContent(globalReferencesPathJson);
        }

        return "";
    }
}
