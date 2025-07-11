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

package org.testingisdocumenting.znai.extensions.json;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.features.PluginFeature;
import org.testingisdocumenting.znai.extensions.file.SnippetAutoTitleFeature;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.stream.Stream;

public class JsonIncludePlugin extends JsonBasePlugin implements IncludePlugin {
    private String fileName;

    @Override
    public IncludePlugin create() {
        return new JsonIncludePlugin();
    }

    @Override
    protected void registerAdditionalParams(PluginParamsDefinition paramsDefinition) {
        paramsDefinition.add(SnippetAutoTitleFeature.paramsDefinition);
    }

    @Override
    protected Stream<PluginFeature> additionalPluginFeatures() {
        return Stream.of(new SnippetAutoTitleFeature(fileName));
    }

    @Override
    protected Stream<AuxiliaryFile> additionalAuxiliaryFiles() {
        return Stream.of(AuxiliaryFile.builtTime(resourcesResolver.fullPath(fileName)));
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        fileName = pluginParams.getFreeParam();
        String json = componentsRegistry.resourceResolver().textContent(fileName);

        return commonProcess(componentsRegistry, markupPath, pluginParams, json);
    }

    @Override
    public String markdownRepresentation() {
        if (resourcesResolver == null) {
            return "";
        }
        
        String json = resourcesResolver.textContent(fileName);
        
        StringBuilder markdown = new StringBuilder();
        markdown.append("```json\n");
        markdown.append(json);
        if (!json.endsWith("\n")) {
            markdown.append("\n");
        }
        markdown.append("```");
        
        return markdown.toString();
    }
}
