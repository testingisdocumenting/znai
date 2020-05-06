/*
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

package org.testingisdocumenting.znai.extensions.svg;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.structure.DocStructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SvgIncludePlugin implements IncludePlugin {
    private AuxiliaryFile svgAuxiliaryFile;

    @Override
    public String id() {
        return "svg";
    }

    @Override
    public IncludePlugin create() {
        return new SvgIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        DocStructure docStructure = componentsRegistry.docStructure();

        String svgSrc = pluginParams.getFreeParam();
        svgAuxiliaryFile = resourcesResolver.runtimeAuxiliaryFile(svgSrc);

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("svgSrc", docStructure.fullUrl(svgAuxiliaryFile.getDeployRelativePath().toString()) +
                "?timestamp=" + timestamp());
        props.putAll(pluginParams.getOpts().toMap());

        return PluginResult.docElement("Svg", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(svgAuxiliaryFile);
    }

    // this is to force cache update on a client side
    private long timestamp() {
        try {
            return Files.getLastModifiedTime(svgAuxiliaryFile.getPath()).toMillis();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
