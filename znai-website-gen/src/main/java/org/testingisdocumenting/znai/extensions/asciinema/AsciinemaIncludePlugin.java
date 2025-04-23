/*
 * Copyright 2025 znai maintainers
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

package org.testingisdocumenting.znai.extensions.asciinema;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.*;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class AsciinemaIncludePlugin implements IncludePlugin {
    private AuxiliaryFile auxiliaryFile;

    @Override
    public String id() {
        return "asciinema";
    }

    @Override
    public PluginParamsDefinition parameters() {
        PluginParamsDefinition params = new PluginParamsDefinition();
        params.add("startAt", PluginParamType.NUMBER_OR_STRING, "start the playback at a given time", "10 (seconds) or \"2:03\" (\"mm:ss\")");
        params.add("poster", PluginParamType.STRING, "preview frame to display", "npt:0:32 (0 minutes 32 seconds)");

        return params;
    }

    @Override
    public IncludePlugin create() {
        return new AsciinemaIncludePlugin();
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(auxiliaryFile);
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        String path = pluginParams.getFreeParam();
        auxiliaryFile = componentsRegistry.resourceResolver().runtimeAuxiliaryFile(path);

        Map<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        props.put("src", componentsRegistry.docStructure().fullUrl(auxiliaryFile.getDeployRelativePath().toString()));

        return PluginResult.docElement("Asciinema", props);
    }
}
