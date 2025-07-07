/*
 * Copyright 2021 znai maintainers
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

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.znai.extensions.cli.CliCommandPropsAndValidation.CliDocElementName;

public class CliFencePlugin implements FencePlugin {
    private List<String> commands;

    @Override
    public String id() {
        return "cli";
    }

    @Override
    public FencePlugin create() {
        return new CliFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        commands = Arrays.stream(content.split("\n"))
                        .map(String::trim)
                        .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());

        Stream<DocElement> docElementStream = commands.stream()
                .map(command -> {
                    Map<String, Object> props = CliCommandPropsAndValidation.createProps(command,
                            content,
                            pluginParams.getOpts());
                    DocElement cliCommand = new DocElement(CliDocElementName);
                    cliCommand.addProps(props);

                    return cliCommand;
                });

        return PluginResult.docElements(docElementStream);
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(String.join(" ", commands));
    }

    @Override
    public String markdownRepresentation(PluginParams params) {
        if (commands == null || commands.isEmpty()) {
            return "";
        }

        return "```cli\n" +
                String.join("\n", commands) +
                "\n```";
    }
}
