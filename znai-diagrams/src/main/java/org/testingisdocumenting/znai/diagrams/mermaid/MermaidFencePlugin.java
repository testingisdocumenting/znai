/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.diagrams.mermaid;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class MermaidFencePlugin implements FencePlugin {
    private String content;

    @Override
    public String id() {
        return "mermaid";
    }

    @Override
    public FencePlugin create() {
        return new MermaidFencePlugin();
    }

    @Override
    public PluginParamsDefinition parameters() {
        return new PluginParamsDefinition()
                .add("wide", PluginParamType.BOOLEAN, "use all the horizontal space for the diagram", "true");
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        this.content = content;
        Map<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        props.put("mermaid", content);

        return PluginResult.docElement("Mermaid", props);
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(this.content);
    }
}
