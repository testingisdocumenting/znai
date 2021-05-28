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

package org.testingisdocumenting.znai.extensions.cli;

import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

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
        PluginParamsOpts opts = pluginParams.getOpts();
        Set<String> combinedParams = new LinkedHashSet<>(opts.getList("paramToHighlight"));

        combinedParams.addAll(opts.getList("paramsToHighlight"));
        combinedParams.addAll(opts.getList("highlight"));

        if (opts.has("paramToHighlight") ||
                opts.has("paramsToHighlight")) {
            ConsoleOutputs.out(Color.RED, "cli-command param(s)ToHighlight will be deprecated, use <highlight> instead"); // TODO deprecation warning API
        }

        resourcesResolver = componentsRegistry.resourceResolver();
        LinkedHashMap<String, Object> props = new LinkedHashMap<>();
        command = extractCommand(pluginParams);

        props.put("command", command);
        props.put("paramsToHighlight", combinedParams);

        validateParamsToHighlight(command, combinedParams);

        if (opts.has("meta")) {
            props.put("meta", opts.get("meta"));
        }

        if (opts.has("threshold")) {
            props.put("threshold", opts.get("threshold"));
        }

        if (opts.has("presentationThreshold")) {
            props.put("presentationThreshold", opts.get("presentationThreshold"));
        }

        Set<String> splitAfter = opts.getSet("splitAfter");
        if (!splitAfter.isEmpty()) {
            props.put("splitAfter", splitAfter);
            validateSplitAfter(command, splitAfter);
        }

        return PluginResult.docElement("CliCommand", props);
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

    private void validateSplitAfter(String command, Set<String> splitAfter) {
        Set<String> commandParts = new HashSet<>(Arrays.asList(command.split(" ")));

        for (String token : splitAfter) {
            if (!commandParts.contains(token)) {
                throw new RuntimeException("split part \"" + token + "\" is not present in command: " + command);
            }
        }
    }

    private void validateParamsToHighlight(String command, Set<String> paramsToHighlight) {
        for (String paramToHighlight : paramsToHighlight) {
            if (!command.contains(paramToHighlight)) {
                throw new RuntimeException("param to highlight \"" + paramToHighlight + "\" " +
                        "is not present in command: " + command);
            }
        }
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
