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

package com.twosigma.znai.parser;

import com.twosigma.znai.utils.ServiceLoaderUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MarkupParsingConfigurations {
    private static Set<MarkupParsingConfiguration> configurations =
            ServiceLoaderUtils.load(MarkupParsingConfiguration.class);

    private MarkupParsingConfigurations() {
    }

    public static void add(MarkupParsingConfiguration configuration) {
        configurations.add(configuration);
    }

    public static MarkupParsingConfiguration byName(String name) {
        List<MarkupParsingConfiguration> configurations = MarkupParsingConfigurations.configurations.stream()
                .filter(c -> name.equals(c.configurationName()))
                .collect(Collectors.toList());

        if (configurations.isEmpty()) {
            throw new RuntimeException("can't find markup configuration for" +
                    " <" + name + ">. Available configurations: \n" + renderAvailable());
        }

        if (configurations.size() > 1) {
            throw new RuntimeException("more than one configuration found for <" + name + ">:\n" + renderAvailable());
        }

        return configurations.get(0);
    }

    private static String renderAvailable() {
        return configurations.stream()
                .map(c -> c.configurationName() + "(" + c.getClass() + ")")
                .collect(Collectors.joining("\n"));
    }
}
