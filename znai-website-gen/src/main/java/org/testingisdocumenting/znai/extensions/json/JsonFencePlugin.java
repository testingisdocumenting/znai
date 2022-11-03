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

package org.testingisdocumenting.znai.extensions.json;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.features.PluginFeature;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;

import java.nio.file.Path;
import java.util.stream.Stream;

public class JsonFencePlugin extends JsonBasePlugin implements FencePlugin {
    @Override
    public FencePlugin create() {
        return new JsonFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                Path markupPath,
                                PluginParams pluginParams,
                                String content) {
        return commonProcess(componentsRegistry, markupPath, pluginParams, content);
    }

    @Override
    protected void updateParams(PluginParamsDefinition paramsDefinition) {
    }

    @Override
    protected Stream<PluginFeature> additionalPluginFeatures() {
        return Stream.empty();
    }

    @Override
    protected Stream<AuxiliaryFile> additionalAuxiliaryFiles() {
        return Stream.of();
    }
}
