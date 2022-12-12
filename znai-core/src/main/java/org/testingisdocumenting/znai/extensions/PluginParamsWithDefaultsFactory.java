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

import org.testingisdocumenting.znai.structure.PageMeta;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PluginParamsWithDefaultsFactory implements PluginParamsFactory {
    private final Map<String, ?> emptyParams = Collections.emptyMap();
    private final Map<String, Map<String, ?>> globalParams = new HashMap<>();
    private final ThreadLocal<Map<String, Map<String, ?>>> pageLocalParams = ThreadLocal.withInitial(HashMap::new);

    @Override
    public PluginParams create(String pluginId, String freeParam, Map<String, ?> opts) {
        Map<String, ?> globalPluginDefaults = globalParams.getOrDefault(pluginId, emptyParams);
        Map<String, ?> localPluginDefaults = pageLocalParams.get().getOrDefault(pluginId, emptyParams);

        Map<String, Object> combinedOpts = new LinkedHashMap<>();
        combinedOpts.putAll(globalPluginDefaults);
        combinedOpts.putAll(localPluginDefaults);
        combinedOpts.putAll(opts);

        return new PluginParams(pluginId, freeParam, combinedOpts);
    }

    public void setGlobalParams(Map<String, Map<String, ?>> globalParams) {
        this.globalParams.clear();
        this.globalParams.putAll(globalParams);
    }

    public void setPageLocalParams(PageMeta pageMeta) {
        Map<String, Map<String, ?>> pageLocalParams = new HashMap<>();
        for (String potentialPluginId : pageMeta.keySet()) {
            if (!Plugins.hasPlugin(potentialPluginId)) {
                continue;
            }

            Object value = pageMeta.getSingleValue(potentialPluginId);
            Map<String, ?> localParams = JsonUtils.deserializeAsMap(value.toString());
            pageLocalParams.put(potentialPluginId, localParams);
        }

        setPageLocalParams(pageLocalParams);
    }

    private void setPageLocalParams(Map<String, Map<String, ?>> pageLocalParams) {
        Map<String, Map<String, ?>> local = this.pageLocalParams.get();
        local.clear();
        local.putAll(pageLocalParams);
    }
}
