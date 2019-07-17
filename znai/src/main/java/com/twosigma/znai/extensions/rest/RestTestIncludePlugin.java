package com.twosigma.znai.extensions.rest;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

public class RestTestIncludePlugin implements IncludePlugin {
    private Path fullPath;

    @Override
    public String id() {
        return "rest-test";
    }

    @Override
    public IncludePlugin create() {
        return new RestTestIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        fullPath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());

        return PluginResult.docElement("WebTauRest", Collections.singletonMap("testArtifact",
                JsonUtils.deserializeAsMap(componentsRegistry.resourceResolver().textContent(fullPath))));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }
}
