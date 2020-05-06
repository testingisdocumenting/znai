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

package org.testingisdocumenting.znai.extensions.cli;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.utils.NumberUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CliOutputIncludePlugin implements IncludePlugin {
    private Path filePath;
    private List<String> lines;
    private ResourcesResolver resourcesResolver;
    private Path highlightFile;

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
        resourcesResolver = componentsRegistry.resourceResolver();
        filePath = resourcesResolver.fullPath(pluginParams.getFreeParam());

        LinkedHashMap<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        lines = readLines(componentsRegistry, filePath);
        props.put("lines", lines);
        props.put("highlight", findHighlightIndexes(pluginParams.getOpts()));

        return PluginResult.docElement("CliOutput", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        Stream<AuxiliaryFile> highlightFileStream = highlightFile != null ?
                Stream.of(AuxiliaryFile.builtTime(highlightFile)) :
                Stream.empty();

        return Stream.concat(
                Stream.of(AuxiliaryFile.builtTime(filePath)),
                highlightFileStream);
    }

    private static List<String> readLines(ComponentsRegistry componentsRegistry, Path filePath) {
        return Arrays.asList(componentsRegistry.resourceResolver().textContent(filePath).split("\n"));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(String.join(" ", lines));
    }

    private List<Object> findHighlightIndexes(PluginParamsOpts opts) {
        List<Object> list = opts.has("highlightFile") ?
                readListFromFile(opts.getString("highlightFile")) :
                opts.getList("highlight");

        return list.stream().flatMap(this::findIndexes).collect(Collectors.toList());
    }

    private List<Object> readListFromFile(String file) {
        highlightFile = resourcesResolver.fullPath(file);
        return Arrays.stream(resourcesResolver.textContent(highlightFile).split("\n")).collect(toList());
    }

    private Stream<Object> findIndexes(Object numberOrText) {
        if (NumberUtils.isInteger(numberOrText.toString())) {
            return Stream.of(numberOrText);
        }

        List<Object> foundIndexes = IntStream.range(0, lines.size())
                .filter(idx -> lines.get(idx).contains(numberOrText.toString()))
                .boxed()
                .collect(toList());

        if (foundIndexes.isEmpty()) {
            throw new RuntimeException("can't find line: " + numberOrText);
        }

        return foundIndexes.stream();
    }
}
