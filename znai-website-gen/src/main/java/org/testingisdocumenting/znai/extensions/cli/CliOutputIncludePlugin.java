/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.extensions.cli;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.features.PluginFeatureList;
import org.testingisdocumenting.znai.extensions.file.SnippetContentProvider;
import org.testingisdocumenting.znai.extensions.file.SnippetHighlightFeature;
import org.testingisdocumenting.znai.extensions.file.SnippetRevealLineStopFeature;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

public class CliOutputIncludePlugin implements IncludePlugin, SnippetContentProvider {
    private String fileName;
    private Path filePath;
    private List<String> lines;

    private PluginFeatureList features;

    @Override
    public String id() {
        return "cli-output";
    }

    @Override
    public IncludePlugin create() {
        return new CliOutputIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        fileName = pluginParams.getFreeParam();
        filePath = resourcesResolver.fullPath(fileName);

        features = new PluginFeatureList(
                new SnippetRevealLineStopFeature(pluginParams, this),
                new SnippetHighlightFeature(componentsRegistry, pluginParams, this)
        );

        LinkedHashMap<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        lines = readLines(componentsRegistry, filePath);
        props.put("lines", lines);
        props.putAll(pluginParams.getOpts().toMap());
        features.updateProps(props);

        return PluginResult.docElement("CliOutput", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(
                Stream.of(AuxiliaryFile.builtTime(filePath)),
                features.auxiliaryFiles());
    }

    private static List<String> readLines(ComponentsRegistry componentsRegistry, Path filePath) {
        return Arrays.asList(componentsRegistry.resourceResolver().textContent(filePath).split("\n"));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(String.join(" ", lines));
    }

    @Override
    public String snippetContent() {
        return String.join("\n", lines);
    }

    @Override
    public String snippetId() {
        return fileName;
    }
}
