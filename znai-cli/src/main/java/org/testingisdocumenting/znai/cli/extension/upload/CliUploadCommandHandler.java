package org.testingisdocumenting.znai.cli.extension.upload;

import org.apache.commons.lang3.StringUtils;
import org.testingisdocumenting.znai.cli.extension.CliCommandConfig;
import org.testingisdocumenting.znai.cli.extension.CliCommandHandler;
import org.testingisdocumenting.znai.cli.extension.CliCommandHandlers;
import org.testingisdocumenting.znai.client.upload.DocUploader;

public class CliUploadCommandHandler implements CliCommandHandler {
    private static final String SERVER_URL = System.getProperty("znai.server.url");

    static {
        if (StringUtils.isNotBlank(SERVER_URL)) {
            CliCommandHandlers.add(new CliUploadCommandHandler());
        }
    }

    @Override
    public String commandName() {
        return "upload";
    }

    @Override
    public String description() {
        return "upload documentation to the global documentation server";
    }

    @Override
    public void handle(CliCommandConfig cliCommandConfig) {
        if (cliCommandConfig.getDocId().equals("no-id-specified")) {
            throw new IllegalArgumentException("--doc-id is required for upload");
        }

        DocUploader.upload(SERVER_URL,
                cliCommandConfig.getDocId(),
                cliCommandConfig.getDeployRoot().resolve(cliCommandConfig.getDocId()),
                cliCommandConfig.getActor());
    }
}
