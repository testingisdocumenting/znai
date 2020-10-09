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
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PluginParams {
    private static final String RIGHT_SIDE_OPT_NAME = "rightSide";
    private static final String STICKY_SLIDE_OPT_NAME = "stickySlide";

    private final String pluginId;
    private String freeParam;
    private PluginParamsOpts opts;

    public static final PluginParams EMPTY = new PluginParams("");

    public PluginParams(String pluginId) {
        this.pluginId = pluginId;
        this.opts = new PluginParamsOpts(Collections.emptyMap());
    }

    public PluginParams(String pluginId, String value) {
        this.pluginId = pluginId;
        this.setValue(value);
    }

    public PluginParams(String pluginId, Map<String, ?> opts) {
        this.pluginId = pluginId;
        this.opts = new PluginParamsOpts(shortcutRightSideOption(opts));
    }

    public void setValue(String value) {
        this.freeParam = extractFreeParam(value);
        this.opts = new PluginParamsOpts(shortcutRightSideOption(extractMap(value)));
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

    private String extractFreeParam(String value) {
        int optsStartIdx = value.indexOf('{');
        return (optsStartIdx != -1 ?
                value.substring(0, optsStartIdx):
                value).trim();
    }

    private Map<String, ?> extractMap(String value) {
        int optsStartIdx = value.indexOf('{');
        if (optsStartIdx == -1) {
            return Collections.emptyMap();
        }

        String json = value.substring(optsStartIdx);
        return JsonUtils.deserializeAsMap(json);
    }

    private Map<String, ?> shortcutRightSideOption(Map<String, ?> opts) {
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
