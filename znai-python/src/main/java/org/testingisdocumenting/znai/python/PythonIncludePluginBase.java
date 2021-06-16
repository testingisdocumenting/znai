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

package org.testingisdocumenting.znai.python;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.features.PluginFeatureList;
import org.testingisdocumenting.znai.extensions.file.CodeReferencesFeature;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract public class PythonIncludePluginBase implements IncludePlugin {
    private PythonIncludeResult pythonResult;
    protected String givenFilePath;
    protected String entryName;
    protected Path fullPath;
    protected ComponentsRegistry componentsRegistry;
    protected PluginParams pluginParams;
    protected PluginFeatureList features;
    protected CodeReferencesFeature codeReferencesFeature;

    abstract public PythonIncludeResult process(PythonCode parsed);

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();

        this.componentsRegistry = componentsRegistry;
        this.pluginParams = pluginParams;

        givenFilePath = pluginParams.getFreeParam();
        if (!resourcesResolver.canResolve(givenFilePath)) {
            throw new IllegalArgumentException("can't find file: " + givenFilePath);
        }

        codeReferencesFeature = new CodeReferencesFeature(componentsRegistry, markupPath, pluginParams);
        features = new PluginFeatureList(
                codeReferencesFeature
        );

        fullPath = resourcesResolver.fullPath(givenFilePath);
        entryName = pluginParams.getOpts().getRequiredString("entry");

        PythonCode pythonParseResult = PythonBasedPythonParser.INSTANCE.parse(fullPath);
        pythonResult = process(pythonParseResult);

        return PluginResult.docElements(pythonResult.getDocElements().stream());
    }

    protected PythonCodeEntry findEntryByName(PythonCode parsed, String name) {
        PythonCodeEntry entry = parsed.findEntryByName(name);
        if (entry == null) {
            throw new RuntimeException("can't find entry: " + name +
                    " in: " + givenFilePath + ", available entries: " +
                    parsed.namesStream().collect(Collectors.joining(", ")));
        }

        return entry;
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(pythonResult.getText());
    }
}
