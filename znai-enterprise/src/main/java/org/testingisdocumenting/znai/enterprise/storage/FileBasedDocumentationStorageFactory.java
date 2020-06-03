package org.testingisdocumenting.znai.enterprise.storage;

import org.testingisdocumenting.znai.server.ZnaiServerConfig;

import static org.testingisdocumenting.znai.enterprise.EnterpriseComponentsRegistry.enterpriseConfig;

public class FileBasedDocumentationStorageFactory implements DocumentationStorageFactory {
    public DocumentationStorage create(ZnaiServerConfig serverConfig) {
        return new FileBasedDocumentationStorage(
                enterpriseConfig().getDocStorageRoot(),
                serverConfig.getDeployRoot());
    }
}
