/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.enterprise.upload;

import org.apache.commons.lang3.StringUtils;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.fs.FsUtils;
import org.testingisdocumenting.znai.server.ZnaiServerConfig;

import java.nio.file.Path;

import static org.testingisdocumenting.znai.enterprise.EnterpriseComponentsRegistry.documentationStorage;

public class UnzipAndStoreOnUploadFinishedServerHandler implements OnUploadFinishedServerHandler {
    private static final String SERVER_URL = System.getProperty("znai.server.url");

    static {
        if (StringUtils.isNotBlank(SERVER_URL)) {
            OnUploadFinishedServerHandlers.add(new UnzipAndStoreOnUploadFinishedServerHandler());
        }
    }

    @Override
    public void onUploadFinished(ZnaiServerConfig config, String docId, Path uploadedPath, String actor) {
        Path unzipDest = config.getDeployRoot().resolve(docId);

        ConsoleOutputs.out(Color.BLUE, "unzipping docs: ", Color.PURPLE, uploadedPath, Color.BLACK, " to ",
                Color.PURPLE, unzipDest);

        FsUtils.unzip(uploadedPath, unzipDest);
        ConsoleOutputs.out(Color.BLUE, "unzipped docs: ", Color.PURPLE, unzipDest);

        documentationStorage().store(docId, "", unzipDest, actor);
    }
}
