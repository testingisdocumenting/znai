/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.extensions.attention;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AttentionSignFencePluginBase implements FencePlugin {
    private MarkupParserResult parserResult;

    @Override
    public String id() {
        return "attention-" + type();
    }

    abstract protected String type();

    @Override
    public PluginParamsDefinition parameters() {
        return new PluginParamsDefinition()
                .add("label", PluginParamType.STRING, "optional label to put next to the icon", "\"Consider\")");
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        MarkupParser markupParser = componentsRegistry.defaultParser();
        parserResult = markupParser.parse(markupPath, content);

        Map<String, Object> props = pluginParams.getOpts().toMap();
        props.put("attentionType", type());
        props.put("content", parserResult.docElement().contentToListOfMaps());

        return PluginResult.docElement("AttentionBlock", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return parserResult.auxiliaryFiles().stream();
    }

    @Override
    public List<SearchText> textForSearch() {
        return List.of(SearchScore.STANDARD.text(parserResult.getAllText()));
    }

}
