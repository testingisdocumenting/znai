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

package org.testingisdocumenting.znai.enterprise;

import org.testingisdocumenting.znai.enterprise.authorization.NixGroupsBasedAuthorizationHandler;
import org.testingisdocumenting.znai.enterprise.landing.LandingDocEntriesProviders;
import org.testingisdocumenting.znai.enterprise.storage.DocumentationStorage;
import org.testingisdocumenting.znai.enterprise.storage.FileBasedDocumentationStorage;
import org.testingisdocumenting.znai.server.ServerLifecycleListener;
import org.testingisdocumenting.znai.server.ZnaiServerConfig;
import org.testingisdocumenting.znai.server.auth.AuthorizationHandlers;

public class EnterpriseComponentsRegistry implements ServerLifecycleListener {
    private static ZnaiServerConfig serverConfig;
    private static final ZnaiEnterpriseConfig enterpriseConfig = new ZnaiEnterpriseConfig();

    private static DocumentationStorage documentationStorage;

    @Override
    public void beforeStart(ZnaiServerConfig config) {
        serverConfig = config;
        documentationStorage = createStorage();

        registerAuthz(config);
    }

    public static DocumentationStorage documentationStorage() {
        return documentationStorage;
    }

    public static ZnaiEnterpriseConfig enterpriseConfig() {
        return enterpriseConfig;
    }

    public static ZnaiServerConfig serverConfig() {
        return serverConfig;
    }

    private static DocumentationStorage createStorage() {
        FileBasedDocumentationStorage documentationStorage = new FileBasedDocumentationStorage(
                enterpriseConfig().getDocStorageRoot(),
                serverConfig.getDeployRoot());

        LandingDocEntriesProviders.add(documentationStorage);

        return documentationStorage;
    }

    private void registerAuthz(ZnaiServerConfig config) {
        if (config.isAuthorizationUsingNixGroups()) {
            AuthorizationHandlers.add(new NixGroupsBasedAuthorizationHandler());
        }
    }
}
