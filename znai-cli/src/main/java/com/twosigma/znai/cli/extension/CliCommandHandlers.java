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
