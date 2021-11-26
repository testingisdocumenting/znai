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

import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;

import java.util.*;

class CliCommandPropsAndValidation {
    static final String CliDocElementName = "CliCommand";

    static Map<String, Object> createProps(String commandToRender,
                                           String commandToValidateProps,
                                           PluginParamsOpts opts) {
        Set<String> combinedParams = new LinkedHashSet<>(opts.getList("paramToHighlight"));

        combinedParams.addAll(opts.getList("paramsToHighlight"));
        combinedParams.addAll(opts.getList("highlight"));

        if (opts.has("paramToHighlight") ||
                opts.has("paramsToHighlight")) {
            ConsoleOutputs.out(Color.RED, "cli param(s)ToHighlight will be deprecated, use <highlight> instead"); // TODO deprecation warning API
        }

        LinkedHashMap<String, Object> props = new LinkedHashMap<>();

        props.put("command", commandToRender);
        props.put("paramsToHighlight", combinedParams);

        validateParamsToHighlight(commandToValidateProps, combinedParams);

        opts.assignToProps(props, "meta");
        opts.assignToProps(props, "threshold");
        opts.assignToProps(props, "presentationThreshold");

        Set<String> splitAfter = opts.getSet("splitAfter");
        if (!splitAfter.isEmpty()) {
            props.put("splitAfter", splitAfter);
            validateSplitAfter(commandToValidateProps, splitAfter);
        }

        return props;
    }

    private static void validateSplitAfter(String command, Set<String> splitAfter) {
        Set<String> commandParts = new HashSet<>(Arrays.asList(command.split(" ")));

        for (String token : splitAfter) {
            if (!commandParts.contains(token)) {
                throw new RuntimeException("split part \"" + token + "\" is not present in command: " + command);
            }
        }
    }

    private static void validateParamsToHighlight(String command, Set<String> paramsToHighlight) {
        for (String paramToHighlight : paramsToHighlight) {
            if (!command.contains(paramToHighlight)) {
                throw new RuntimeException("param to highlight \"" + paramToHighlight + "\" " +
                        "is not present in command: " + command);
            }
        }
    }
}
