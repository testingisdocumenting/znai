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

package org.testingisdocumenting.znai.extensions;

import org.testingisdocumenting.znai.extensions.meta.MetaIncludePlugin;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PluginParams {
    private static final String RIGHT_SIDE_OPT_NAME = "rightSide";
    private static final String STICKY_SLIDE_OPT_NAME = "stickySlide";

    private final String pluginId;
    private final String freeParam;
    private final PluginParamsOpts opts;

    public static final PluginParams EMPTY = new PluginParams("", "", Collections.emptyMap());

    PluginParams(String pluginId, String freeParam, Map<String, ?> opts) {
        this.pluginId = pluginId;
        this.freeParam = freeParam;
        this.opts = new PluginParamsOpts(pluginId, shortcutMetaOptions(opts));
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getFreeParam() {
        return freeParam;
    }

    public PluginParamsOpts getOpts() {
        return opts;
    }

    private Map<String, ?> shortcutMetaOptions(Map<String, ?> opts) {
        Map<String, Object> result = new LinkedHashMap<>(opts);

        Object rightSide = opts.get(RIGHT_SIDE_OPT_NAME);
        Object stickySlide = opts.get(STICKY_SLIDE_OPT_NAME);
        if (rightSide == null && stickySlide == null) {
            return result;
        }

        if (pluginId.equals(MetaIncludePlugin.ID)) {
            return result;
        }

        Map<String, Object> meta = new LinkedHashMap<>();

        if (rightSide != null) {
            meta.put(RIGHT_SIDE_OPT_NAME, rightSide);
        }

        if (stickySlide != null) {
            meta.put(STICKY_SLIDE_OPT_NAME, stickySlide);
        }

        result.put("meta", meta);
        result.remove(RIGHT_SIDE_OPT_NAME);
        result.remove(STICKY_SLIDE_OPT_NAME);

        return result;
    }
}
