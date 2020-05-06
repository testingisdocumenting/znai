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

package org.testingisdocumenting.znai.extensions.tabs;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.ColonDelimitedKeyValues;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class TabsFencePlugin implements FencePlugin {
    private Path markupPath;
    private MarkupParser parser;

    private List<String> texts;
    private List<AuxiliaryFile> auxiliaryFiles;

    @Override
    public String id() {
        return "tabs";
    }

    @Override
    public FencePlugin create() {
        return new TabsFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        this.markupPath = markupPath;
        this.parser = componentsRegistry.defaultParser();
        this.texts = new ArrayList<>();
        this.auxiliaryFiles = new ArrayList<>();

        ColonDelimitedKeyValues tabsDefinitions = new ColonDelimitedKeyValues(content);
        List<ParsedTab> parsedTabs = tabsDefinitions.map(this::parseTab).collect(toList());

        Map<String, Object> tabsProps = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        tabsProps.put("tabsContent", parsedTabs.stream().map(this::tabProps).collect(toList()));

        parsedTabs.forEach(this::generateSearchText);
        parsedTabs.forEach(this::collectAuxiliaryFiles);

        return PluginResult.docElement("Tabs", tabsProps);
    }

    private Map<String, Object> tabProps(ParsedTab parsedTab) {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("name", parsedTab.name);
        props.put("content", parsedTab.parserResult.contentToListOfMaps());

        return props;
    }

    private void generateSearchText(ParsedTab parsedTab) {
        texts.add(parsedTab.name);
        texts.add(parsedTab.parserResult.getAllText());
    }

    private void collectAuxiliaryFiles(ParsedTab parsedTab) {
        auxiliaryFiles.addAll(parsedTab.parserResult.getAuxiliaryFiles());
    }

    private ParsedTab parseTab(String tabName, String markup) {
        MarkupParserResult parserResult = parser.parse(markupPath, markup);
        return new ParsedTab(tabName, parserResult);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return auxiliaryFiles.stream();
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(String.join(" ", texts));
    }

    private static class ParsedTab {
        private String name;
        private MarkupParserResult parserResult;

        private ParsedTab(String name, MarkupParserResult parserResult) {
            this.name = name;
            this.parserResult = parserResult;
        }
    }
}
