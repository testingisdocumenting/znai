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

package org.testingisdocumenting.znai.extensions.attention;

import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;

public class AttentionSignCustomFencePlugin extends AttentionSignFencePluginBase {
    @Override
    protected String type() {
        return "custom";
    }

    @Override
    public PluginParamsDefinition parameters() {
        return super.parameters()
                .add("icon", PluginParamType.STRING, "optional icon id to display next to the content, " +
                        "no icon is used when not specified", "\"info\"");
    }

    @Override
    protected String attentionType(PluginParams pluginParams) {
        String customType = pluginParams.getFreeParam();
        if (customType == null || customType.isBlank()) {
            throw new IllegalArgumentException("attention-custom requires a type as the first free form parameter, " +
                    "e.g. ```attention-custom my-type");
        }

        return customType.trim();
    }

    @Override
    public FencePlugin create() {
        return new AttentionSignCustomFencePlugin();
    }
}
