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

package org.testingisdocumenting.znai.enterprise.docpreparation;

import org.apache.commons.lang3.StringUtils;
import org.testingisdocumenting.znai.enterprise.storage.DocumentationFileBasedTimestamp;
import org.testingisdocumenting.znai.server.docpreparation.DocumentationPreparationHandler;
import org.testingisdocumenting.znai.server.docpreparation.DocumentationPreparationHandlers;
import org.testingisdocumenting.znai.server.docpreparation.DocumentationPreparationProgress;

import static org.testingisdocumenting.znai.enterprise.EnterpriseComponentsRegistry.*;

public class StorageBasedDocumentationPreparationHandler implements DocumentationPreparationHandler {
    private static final String SERVER_URL = System.getProperty("znai.server.url");

    static {
        if (StringUtils.isNotBlank(SERVER_URL)) {
            DocumentationPreparationHandlers.add(new StorageBasedDocumentationPreparationHandler());
        }
    }

    @Override
    public boolean handles(String docId) {
        return enterpriseConfig().getDocStorageRoot() != null &&
                documentationStorage().contains(docId);
    }

    @Override
    public boolean isReady(String docId) {
        long storageUpdateTime = documentationStorage().lastUpdateTime(docId, "");
        long currentTimestamp = DocumentationFileBasedTimestamp.read(serverConfig().getDeployRoot().resolve(docId));

        return storageUpdateTime <= currentTimestamp;
    }

    @Override
    public void prepare(String docId, DocumentationPreparationProgress preparationProgress) {
        documentationStorage().prepare(docId, "", preparationProgress);
    }
}