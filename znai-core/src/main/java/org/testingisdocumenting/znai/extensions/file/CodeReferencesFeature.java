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

package org.testingisdocumenting.znai.extensions.file;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.features.PluginFeature;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.reference.DocReferencesParser;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.structure.DocUrl;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

/**
 * common code for the include-file plugin, include-java plugin, etc
 * to handle parsing of code references, converting them to props, and handling auxiliary files.
 */
public class CodeReferencesFeature implements PluginFeature {
    public static final PluginParamsDefinition paramsDefinition = createParamsDefinition();

    private static final String REFERENCES_PATH_KEY = "referencesPath";

    private final ComponentsRegistry componentsRegistry;

    private final Path referencesFullPath;
    private final String referencesPath;

    private final Path markupPath;
    private final DocReferences references;

    public CodeReferencesFeature(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        this.componentsRegistry = componentsRegistry;
        this.markupPath = markupPath;

        this.referencesPath = pluginParams.getOpts().get(REFERENCES_PATH_KEY, null);
        this.referencesFullPath = referencesPath != null ?
                componentsRegistry.resourceResolver().fullPath(referencesPath):
                null;

        this.references = buildReferences();
    }

    public DocReferences getReferences() {
        return references;
    }

    public void updateProps(Map<String, Object> props) {
        if (!referencesProvided()) {
            return;
        }

        validateLinks(references);
        props.put("references", references.toMap());
    }

    private void validateLinks(DocReferences references) {
        DocStructure docStructure = componentsRegistry.docStructure();

        references.pageUrlsStream().forEach(pageUrl ->
                docStructure.validateUrl(markupPath,
                        "reference file name: " + referencesFullPath.getFileName().toString(),
                        new DocUrl(pageUrl))
        );
    }

    private DocReferences buildReferences() {
        if (referencesPath == null) {
            return DocReferences.EMPTY;
        }

        return DocReferencesParser.parse(
                componentsRegistry.resourceResolver().textContent(referencesPath));
    }

    public Stream<AuxiliaryFile> auxiliaryFiles() {
        return referencesProvided() ?
                Stream.of(AuxiliaryFile.builtTime(referencesFullPath)) : Stream.empty();
    }

    private boolean referencesProvided() {
        return referencesFullPath != null;
    }

    private static PluginParamsDefinition createParamsDefinition() {
        return new PluginParamsDefinition()
                .add(REFERENCES_PATH_KEY, PluginParamType.STRING, "path to a file with code references", "\"myreferences.csv\"");
    }
}
