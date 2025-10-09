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

package org.testingisdocumenting.znai.extensions.inlinedcode;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Use <code>identifier</code> inlined code plugin to render inlined code and validate
 * its spelling by looking at the provided file for match. Use it to make sure
 * your documentation reflects the API changes and do not use outdated identifier names
 * <pre>
 * `:identifier: my_func {validationPath: "my_func_usage.py"}`
 * </pre>
 */
public class IdentifierInlinedCodePlugin implements InlinedCodePlugin {
    private String identifier;
    private List<String> validationPaths;

    @Override
    public String id() {
        return "identifier";
    }

    @Override
    public InlinedCodePlugin create() {
        return new IdentifierInlinedCodePlugin();
    }

    @Override
    public PluginParamsDefinition parameters() {
        return new PluginParamsDefinition()
                .addRequired("validationPath", PluginParamType.LIST_OR_SINGLE_STRING,
                        "single file, or a list of files to use for the identifier validation",
                        "[\"file1.py\", \"file2.py\"]");
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        identifier = pluginParams.getFreeParam();
        validationPaths = pluginParams.getOpts().getList("validationPath");

        validate(componentsRegistry.resourceResolver());

        return PluginResult.docElement(DocElementType.INLINED_CODE, Collections.singletonMap("code", identifier));
    }

    private void validate(ResourcesResolver resourcesResolver) {
        if (validationPaths.isEmpty()) {
            throw new IllegalArgumentException("validationPath is missing");
        }

        Pattern identifierRegexp = Pattern.compile("\\b" + identifier + "\\b", Pattern.MULTILINE);
        boolean hasIdentifier = validationPaths.stream().anyMatch(path -> hasIdentifierInFile(resourcesResolver, path, identifierRegexp));

        if (!hasIdentifier) {
            throw new RuntimeException("can't find <" + identifier + "> identifier in: " + String.join(", ", validationPaths));
        }
    }

    private boolean hasIdentifierInFile(ResourcesResolver resourceResolver, String path, Pattern identifierRegexp) {
        String validationContent = resourceResolver.textContent(path);
        Matcher matcher = identifierRegexp.matcher(validationContent);

        return matcher.find();
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return validationPaths.stream().map(path ->
                AuxiliaryFile.builtTime(componentsRegistry.resourceResolver().fullPath(path)));
    }

    @Override
    public List<SearchText> textForSearch() {
        return List.of(SearchScore.STANDARD.text(identifier));
    }
}
