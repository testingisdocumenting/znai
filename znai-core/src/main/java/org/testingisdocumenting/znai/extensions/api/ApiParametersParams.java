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

package org.testingisdocumenting.znai.extensions.api;

import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;

class ApiParametersParams {
    public static final PluginParamsDefinition definition = new PluginParamsDefinition()
            .add("anchorPrefix", PluginParamType.STRING,
                    "prefix to use for individual parameter link anchors", "\"my-api\"")
            .add("title", PluginParamType.STRING,
                    "api parameters title", "\"my api\"")
            .add("small", PluginParamType.BOOLEAN,
                    "use smaller font and width", "true")
            .add("noWrap", PluginParamType.BOOLEAN,
                    "do not wrap long parameter names and instead expand parameters column width to fit", "true");
}
