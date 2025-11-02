/*
 * Copyright 2025 znai maintainers
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

package org.testingisdocumenting.znai.extensions.reveal;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.markdown.PageMarkdownSection;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.utils.CollectionUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadMoreFencePlugin implements FencePlugin {
    private static final String TITLE_KEY = "title";
    private MarkupParserResult parserResult ;

    @Override
    public String id() {
        return "readmore";
    }

    @Override
    public FencePlugin create() {
        return new ReadMoreFencePlugin();
    }

    @Override
    public PluginParamsDefinition parameters() {
        PluginParamsDefinition params = new PluginParamsDefinition();
        params.add(TITLE_KEY, PluginParamType.STRING, "title to display before user clicks on it to reveal more", "\"Usage of low-level API\"");
        return params;
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        MarkupParser markupParser = componentsRegistry.defaultParser();
        parserResult = markupParser.parse(markupPath, content);

        String title = pluginParams.getOpts().getRequiredString(TITLE_KEY);
        return PluginResult.docElement("ReadMore", CollectionUtils.createMap(
                "title", title,
                "content", parserResult.contentToListOfMaps()));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return parserResult.auxiliaryFiles().stream();
    }

    @Override
    public List<SearchText> textForSearch() {
        return List.of(SearchScore.STANDARD.text(parserResult.getAllText()));
    }

    @Override
    public String markdownRepresentation() {
        if (parserResult == null || parserResult.markdown() == null) {
            return "";
        }

        return parserResult.markdown().sections().stream()
                .map(PageMarkdownSection::markdown)
                .collect(Collectors.joining("\n\n"));
    }
}
