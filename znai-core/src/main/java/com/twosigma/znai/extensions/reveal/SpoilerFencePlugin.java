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

package com.twosigma.znai.extensions.reveal;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;
import com.twosigma.znai.utils.CollectionUtils;

import java.nio.file.Path;
import java.util.stream.Stream;

public class SpoilerFencePlugin implements FencePlugin {
    private MarkupParserResult parserResult ;

    @Override
    public String id() {
        return "spoiler";
    }

    @Override
    public FencePlugin create() {
        return new SpoilerFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        MarkupParser markupParser = componentsRegistry.defaultParser();
        parserResult = markupParser.parse(markupPath, content);

        String title = pluginParams.getOpts().get("title", "Press to reveal");
        return PluginResult.docElement("Spoiler", CollectionUtils.createMap(
                "title", title,
                "content", parserResult.contentToListOfMaps()));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return parserResult.getAuxiliaryFiles().stream();
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.LOW.text(parserResult.getAllText());
    }
}
