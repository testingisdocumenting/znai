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

package org.testingisdocumenting.znai.extensions.reveal;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.utils.CollectionUtils;

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
