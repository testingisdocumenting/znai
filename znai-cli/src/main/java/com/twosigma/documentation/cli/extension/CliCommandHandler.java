package com.twosigma.documentation.cli.extension;

public interface CliCommandHandler {
    String commandName();
    String description();
    void handle(CliCommandConfig config);
}
