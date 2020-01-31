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

package com.twosigma.znai.extensions.api;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;

import java.nio.file.Path;
import java.util.Map;

public class ApiParametersFencePlugin implements FencePlugin {
    @Override
    public String id() {
        return "api-parameters";
    }

    @Override
    public FencePlugin create() {
        return new ApiParametersFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        ApiParameters apiParameters = ApiParametersCsvParser.parse(componentsRegistry.markdownParser(), content);
        Map<String, Object> props = apiParameters.toMap();
        props.putAll(pluginParams.getOpts().toMap());

        return PluginResult.docElement("ApiParameters", props);
    }
}
