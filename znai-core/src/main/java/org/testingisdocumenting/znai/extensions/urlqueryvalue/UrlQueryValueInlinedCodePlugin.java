/*
 * Copyright 2026 znai maintainers
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

package org.testingisdocumenting.znai.extensions.urlqueryvalue;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlQueryValueInlinedCodePlugin implements InlinedCodePlugin {
    @Override
    public String id() {
        return "url-query-value";
    }

    @Override
    public InlinedCodePlugin create() {
        return new UrlQueryValueInlinedCodePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        String queryParam = pluginParams.getFreeParam();

        Map<String, Object> props = new HashMap<>(pluginParams.getOpts().toMap());
        props.put("queryParam", queryParam);

        return PluginResult.docElement("UrlQueryValue", props);
    }

    @Override
    public List<SearchText> textForSearch() {
        return List.of();
    }
}
