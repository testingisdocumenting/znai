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

package com.twosigma.znai.extensions.cli;

import com.twosigma.znai.console.ConsoleOutputs;
import com.twosigma.znai.console.ansi.Color;
import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginParamsOpts;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;

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

        if (opts.has("paramToHighlight")) {
            ConsoleOutputs.out(Color.RED, "cli-command paramToHighlight will be deprecated"); // TODO deprecation warning API
        }

        combinedParams.addAll(opts.getList("paramsToHighlight"));

        resourcesResolver = componentsRegistry.resourceResolver();
        LinkedHashMap<String, Object> props = new LinkedHashMap<>();
        command = extractCommand(pluginParams);

        props.put("command", command);
        props.put("paramsToHighlight", combinedParams);

        validateParamsToHighlight(command, combinedParams);

        if (opts.has("threshold")) {
            props.put("threshold", opts.get("threshold"));
        }

        if (opts.has("presentationThreshold")) {
            props.put("presentationThreshold", opts.get("presentationThreshold"));
        }

        List<String> splitAfter = opts.getList("splitAfter");
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

    private void validateSplitAfter(String command, List<String> splitParts) {
        String[] commandParts = command.split(" ");

        for (String splitPart : splitParts) {
            if (Arrays.stream(commandParts).noneMatch(commandPart -> commandPart.equals(splitPart))) {
                throw new RuntimeException("split part \"" + splitPart + "\" is not present in command: " + command);
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
