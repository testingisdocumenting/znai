/*
 * Copyright 2024 znai maintainers
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

package org.testingisdocumenting.znai.diagrams.graphviz;

import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.paramtypes.PluginParamTypeEnum;

import java.util.Arrays;
import java.util.List;

class GraphvizPluginParams {
    static final String ALIGN_KEY = "align";
    private static final List<String> ALIGN_VALUES = Arrays.asList("left", "center", "right");
    static PluginParamsDefinition definition = create();

    private static PluginParamsDefinition create() {
        PluginParamsDefinition result = new PluginParamsDefinition();
        result.add(ALIGN_KEY, new PluginParamTypeEnum(ALIGN_VALUES), "horizontal diagram alignment");

        return result;
    }
}
