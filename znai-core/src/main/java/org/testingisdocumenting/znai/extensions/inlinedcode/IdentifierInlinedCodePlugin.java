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
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Collections;
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
    private String validationPath;

    @Override
    public String id() {
        return "identifier";
    }

    @Override
    public InlinedCodePlugin create() {
        return new IdentifierInlinedCodePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        identifier = pluginParams.getFreeParam();
        validationPath = pluginParams.getOpts().getRequiredString("validationPath");

        String validationContent = componentsRegistry.resourceResolver().textContent(validationPath);
        validate(validationContent, identifier);

        return PluginResult.docElement(DocElementType.INLINED_CODE, Collections.singletonMap("code", identifier));
    }

    private void validate(String validationContent, String identifier) {
        Pattern pattern = Pattern.compile("\\b" + identifier + "\\b", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(validationContent);
        if (!matcher.find()) {
            throw new RuntimeException("can't find <" + identifier + "> identifier in: " + validationPath);
        }
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(
                componentsRegistry.resourceResolver().fullPath(validationPath)));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(identifier);
    }
}
