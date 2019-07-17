package com.twosigma.znai.cli.extension;

public interface CliCommandHandler {
    String commandName();
    String description();
    void handle(CliCommandConfig config);
}
