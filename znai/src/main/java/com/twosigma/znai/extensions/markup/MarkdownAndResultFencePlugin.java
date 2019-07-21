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

package com.twosigma.znai.extensions.markup;

import com.twosigma.znai.codesnippets.CodeSnippetsProps;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class MarkdownAndResultFencePlugin implements FencePlugin {
    @Override
    public String id() {
        return "markdown-and-result";
    }

    @Override
    public FencePlugin create() {
        return new MarkdownAndResultFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        MarkupParser parser = componentsRegistry.defaultParser();
        MarkupParserResult parserResult = parser.parse(markupPath, content);

        Map<String, Object> markdown = CodeSnippetsProps.create("markdown", content);
        markdown.put("type", DocElementType.SNIPPET);

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("markdown", markdown);
        props.put("result", parserResult.contentToListOfMaps());

        return PluginResult.docElement("MarkdownAndResult", props);
    }
}
