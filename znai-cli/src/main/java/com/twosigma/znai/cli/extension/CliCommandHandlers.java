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

package com.twosigma.znai.cli.extension;

import com.twosigma.utils.ServiceLoaderUtils;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CliCommandHandlers {
    private static Set<CliCommandHandler> handlers = ServiceLoaderUtils.load(CliCommandHandler.class);

    public static void add(CliCommandHandler handler) {
        handlers.add(handler);
    }

    public static void forEach(Consumer<CliCommandHandler> consumer) {
        handlers.forEach(consumer);
    }

    public static boolean hasHandler(String command) {
        return handlers.stream().anyMatch(h -> h.commandName().equals(command));
    }

    public static Stream<String> registeredCommandNames() {
        return handlers.stream().map(CliCommandHandler::commandName);
    }

    public static CliCommandHandler findByCommand(String command) {
        return handlers.stream()
                .filter(h -> h.commandName().equals(command))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("can't find command handler for command: " + command));
    }
}
