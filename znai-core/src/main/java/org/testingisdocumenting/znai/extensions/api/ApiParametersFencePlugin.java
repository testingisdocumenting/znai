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

package org.testingisdocumenting.znai.extensions.api;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Map;

public class ApiParametersFencePlugin implements FencePlugin {
    private ApiParameters apiParameters;

    @Override
    public String id() {
        return "api-parameters";
    }

    @Override
    public FencePlugin create() {
        return new ApiParametersFencePlugin();
    }

    @Override
    public PluginParamsDefinition parameters() {
        return ApiParametersParams.definition;
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        apiParameters = ApiParametersCsvParser.parse(
                pluginParams.getOpts().get("anchorPrefix", ""),
                componentsRegistry.markdownParser(), content);

        ApiParametersAnchors.registerLocalAnchors(componentsRegistry, markupPath, apiParameters);

        Map<String, Object> props = apiParameters.toMap();
        props.putAll(pluginParams.getOpts().toMap());

        return PluginResult.docElement("ApiParameters", props);
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(apiParameters.combinedTextForSearch());
    }
}
