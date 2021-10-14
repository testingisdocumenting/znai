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

package org.testingisdocumenting.znai.cli.extension.upload;

import org.testingisdocumenting.znai.cli.extension.CliCommandConfig;
import org.testingisdocumenting.znai.cli.extension.CliCommandHandler;
import org.testingisdocumenting.znai.client.upload.DocUploader;

public class CliUploadZipCommandHandler implements CliCommandHandler {
    @Override
    public String commandName() {
        return "uploadzip";
    }

    @Override
    public String description() {
        return "upload documentation to the global documentation server";
    }

    @Override
    public void handle(CliCommandConfig cliCommandConfig) {
        if (!cliCommandConfig.isDocIdSpecified()) {
            throw new IllegalArgumentException("--doc-id is required for upload");
        }

        DocUploader.uploadZip(CliUploadCfg.getServerUrl(),
                cliCommandConfig.getDocId(),
                cliCommandConfig.getDeployRoot(),
                cliCommandConfig.getActor());
    }

    @Override
    public boolean needsDocGeneration() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return CliUploadCfg.isEnabled();
    }
}
