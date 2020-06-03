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

package org.testingisdocumenting.znai.enterprise.remove;

import org.apache.commons.lang3.StringUtils;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.server.ZnaiServerConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.testingisdocumenting.znai.enterprise.EnterpriseComponentsRegistry.documentationStorage;

public class OnRemoveDeleteCachedServerHandler implements OnRemoveFinishedServerHandler {
    private static final String SERVER_URL = System.getProperty("znai.server.url");

    static {
        if (StringUtils.isNotBlank(SERVER_URL)) {
            OnRemoveFinishedServerHandlers.add(new OnRemoveDeleteCachedServerHandler());
        }
    }

    @Override
    public void onRemoveFinished(ZnaiServerConfig config, String docId, String actor) {
        Path docPath = config.getDeployRoot().resolve(docId);

        ConsoleOutputs.out(Color.BLUE, "deleting docs: ", Color.PURPLE, docId, Color.BLACK, " at ",
                Color.PURPLE, docPath);

        File docDirectory = docPath.toFile();
        try {
            if (docDirectory.exists()) {
                org.apache.commons.io.FileUtils.deleteDirectory(docDirectory);
            }
            ConsoleOutputs.out(Color.BLUE, "deleted docs: ", Color.PURPLE, docPath, " from file system.");

            documentationStorage().remove(docId, actor);
            ConsoleOutputs.out(Color.BLUE, "deleted docs: ", Color.PURPLE, docPath, " from stroage.");
        } catch (IOException e) {
            ConsoleOutputs.out(Color.BLUE, "failed to delete docs: ", Color.PURPLE, docPath);
        }
    }
}
