package com.twosigma.documentation.extensions.rest;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * @author mykola
 */
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
