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

package org.testingisdocumenting.znai.extensions;

import org.testingisdocumenting.znai.utils.JsonParseException;

import java.util.Map;

public interface PluginParamsFactory {
    PluginParams create(String pluginId, String freeParam, Map<String, ?> opts);

    default PluginParams create(String pluginId, String freeParamAndOptsUnparsed) {
        String freeParam = PluginParamsParser.extractFreeParam(freeParamAndOptsUnparsed);
        try {
            Map<String, ?> opts = PluginParamsParser.extractMap(freeParamAndOptsUnparsed);
            return create(pluginId, freeParam, opts);
        } catch (JsonParseException e) {
            throw new PluginParamsParseException(pluginId, freeParamAndOptsUnparsed, e.getMessage());
        }
    }
}
