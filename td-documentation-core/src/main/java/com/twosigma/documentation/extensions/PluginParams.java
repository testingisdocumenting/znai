package com.twosigma.documentation.extensions;

import com.twosigma.documentation.extensions.meta.MetaIncludePlugin;
import com.twosigma.utils.JsonUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PluginParams {
    private static final String RIGHT_SIDE_OPT_NAME = "rightSide";

    private String pluginId;
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
        if (rightSide == null) {
            return result;
        }

        if (!pluginId.equals(MetaIncludePlugin.ID)) {
            result.put("meta", Collections.singletonMap(RIGHT_SIDE_OPT_NAME, rightSide));
            result.remove(RIGHT_SIDE_OPT_NAME);
        }

        return result;
    }
}
