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

import org.testingisdocumenting.znai.enterprise.authorization.EnterpriseAuthorizationHandler;
import org.testingisdocumenting.znai.enterprise.authorization.groups.AuthorizationGroupResolutionServices;
import org.testingisdocumenting.znai.enterprise.authorization.groups.NixAuthorizationGroupResolutionService;
import org.testingisdocumenting.znai.enterprise.landing.StorageBasedLandingDocEntriesProvider;
import org.testingisdocumenting.znai.enterprise.landing.LandingDocEntriesProviders;
import org.testingisdocumenting.znai.enterprise.storage.DocumentationStorage;
import org.testingisdocumenting.znai.enterprise.storage.DocumentationStorageFactories;
import org.testingisdocumenting.znai.server.ServerLifecycleListener;
import org.testingisdocumenting.znai.server.ZnaiServerConfig;
import org.testingisdocumenting.znai.server.auth.AuthorizationHandlers;

public class EnterpriseComponentsRegistry implements ServerLifecycleListener {
    private static final ZnaiEnterpriseConfig enterpriseConfig = new ZnaiEnterpriseConfig();
    private static ZnaiServerConfig serverConfig;
    private static DocumentationStorage documentationStorage;

    public static DocumentationStorage documentationStorage() {
        return documentationStorage;
    }

    public static ZnaiEnterpriseConfig enterpriseConfig() {
        return enterpriseConfig;
    }

    public static ZnaiServerConfig serverConfig() {
        return serverConfig;
    }

    @Override
    public void beforeStart(ZnaiServerConfig config) {
        serverConfig = config;
        documentationStorage = createStorage();

        registerLanding();
        registerAuthorization();
    }

    private static DocumentationStorage createStorage() {
        return DocumentationStorageFactories.create(serverConfig);
    }

    private void registerLanding() {
        StorageBasedLandingDocEntriesProvider entriesProvider = new StorageBasedLandingDocEntriesProvider();

        LandingDocEntriesProviders.add(entriesProvider);
        DocLifecycleListeners.add(entriesProvider);
    }

    private void registerAuthorization() {
        EnterpriseAuthorizationHandler authorizationHandler = new EnterpriseAuthorizationHandler();

        AuthorizationHandlers.add(authorizationHandler);
        DocLifecycleListeners.add(authorizationHandler);

        if (enterpriseConfig.getAuthzGroupsResolutionType().equals("nix-groups")) {
            AuthorizationGroupResolutionServices.add(new NixAuthorizationGroupResolutionService());
        }
    }
}
