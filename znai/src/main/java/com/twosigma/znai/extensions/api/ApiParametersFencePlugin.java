package com.twosigma.znai.extensions.api;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;

import java.nio.file.Path;

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
        return PluginResult.docElement("ApiParameters", apiParameters.toMap());
    }
}
