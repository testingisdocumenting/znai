/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.extensions.cli;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import static org.testingisdocumenting.znai.extensions.cli.CliCommandPropsAndValidation.CliDocElementName;

public class CliCommandIncludePlugin implements IncludePlugin {
    private String command;
    private ResourcesResolver resourcesResolver;
    private String commandFile;

    @Override
    public String id() {
        return "cli-command";
    }

    @Override
    public IncludePlugin create() {
        return new CliCommandIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        resourcesResolver = componentsRegistry.resourceResolver();
        command = extractCommand(pluginParams);
        Map<String, Object> props = CliCommandPropsAndValidation.createProps(command, command,
                pluginParams.getOpts());

        return PluginResult.docElement(CliDocElementName, props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return commandFile != null && !commandFile.isEmpty() ?
                Stream.of(AuxiliaryFile.builtTime(resourcesResolver.fullPath(commandFile))) :
                Stream.empty();
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(command);
    }

    private String extractCommand(PluginParams pluginParams) {
        String commandAsFreeParam = pluginParams.getFreeParam().trim();
        if (!commandAsFreeParam.isEmpty()) {
            return commandAsFreeParam;
        }

        PluginParamsOpts opts = pluginParams.getOpts();

        commandFile = opts.get("commandFile", "");
        if (!commandFile.isEmpty()) {
            return resourcesResolver.textContent(commandFile);
        }

        return opts.getRequiredString("command");
    }
}
