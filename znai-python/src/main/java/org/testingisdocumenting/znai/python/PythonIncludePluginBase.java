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
import org.testingisdocumenting.znai.extensions.file.AnchorFeature;
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
    protected Path fullPath;
    protected ComponentsRegistry componentsRegistry;
    protected PluginParams pluginParams;
    protected PluginFeatureList features;
    protected CodeReferencesFeature codeReferencesFeature;
    protected ResourcesResolver resourcesResolver;

    abstract public PythonIncludeResult process(PythonParsedFile parsed, ParserHandler parserHandler, Path markupPath);

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        this.componentsRegistry = componentsRegistry;
        this.resourcesResolver = componentsRegistry.resourceResolver();

        this.pluginParams = pluginParams;

        String fileNameToUse = fileNameToUse();
        fullPath = resourcesResolver.fullPath(fileNameToUse);

        codeReferencesFeature = new CodeReferencesFeature(componentsRegistry, markupPath, pluginParams);
        features = new PluginFeatureList(
                codeReferencesFeature,
                new AnchorFeature(componentsRegistry.docStructure(), markupPath, pluginParams)
        );

        PythonContext context = new PythonContext(fileNameToUse, defaultPackageName());
        PythonParsedFile pythonParseResult = Python.INSTANCE.parseFileOrGetCached(fullPath, context);
        pythonResult = process(pythonParseResult, parserHandler, markupPath);

        return PluginResult.docElements(pythonResult.docElements().stream());
    }

    protected String getEntryName() {
        return pluginParams.getOpts().getRequiredString("entry");
    }

    protected String snippetIdToUse() {
        return pluginParams.getFreeParam();
    }

    protected Stream<String> additionalAuxiliaryFileNames() {
        return Stream.empty();
    }

    /**
     * when parsing encounters a type without a package, it prepends it with a default package name
     *
     * @return default package name
     */
    protected String defaultPackageName() {
        return pluginParams.getOpts().get("packageName", "");
    }

    protected String fileNameToUse() {
        return pluginParams.getFreeParam();
    }

    protected PythonParsedEntry findEntryByName(PythonParsedFile parsed, String name) {
        PythonParsedEntry entry = parsed.findEntryByName(name);
        if (entry == null) {
            throw new RuntimeException("can't find entry: " + name +
                    " in: " + snippetIdToUse() + ", available entries: " +
                    parsed.namesStream().collect(Collectors.joining(", ")));
        }

        return entry;
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(
                Stream.of(AuxiliaryFile.builtTime(fullPath)),
                additionalAuxiliaryFileNames().map(fileName -> AuxiliaryFile.builtTime(resourcesResolver.fullPath(fileName))));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(pythonResult.text());
    }
}
