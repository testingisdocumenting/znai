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

package org.testingisdocumenting.znai.extensions.templates;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.ColonDelimitedKeyValues;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.template.TextTemplate;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TemplateFencePlugin implements FencePlugin {
    private Path fullPath;
    private MarkupParserResult parserResult;

    @Override
    public String id() {
        return "template";
    }

    @Override
    public FencePlugin create() {
        return new TemplateFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        MarkupParser parser = componentsRegistry.defaultParser();

        fullPath = resourcesResolver.fullPath(pluginParams.getFreeParam());
        parserResult = parser.parse(markupPath, processTemplate(resourcesResolver.textContent(fullPath),
                new ColonDelimitedKeyValues(content)));

        return PluginResult.docElements(parserResult.docElement().getContent().stream());
    }

    private String processTemplate(String template, ColonDelimitedKeyValues keyValues) {
        return new TextTemplate(fullPath.getFileName().toString(), template)
                .process(Collections.unmodifiableMap(keyValues.toMap()));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(parserResult.auxiliaryFiles().stream(),
                Stream.of(AuxiliaryFile.builtTime(fullPath)));
    }

    @Override
    public List<SearchText> textForSearch() {
        return List.of(SearchScore.STANDARD.text(parserResult.getAllText()));
    }
}
