package org.testingisdocumenting.znai.enterprise.storage;

import org.testingisdocumenting.znai.server.ZnaiServerConfig;

public interface DocumentationStorageFactory {
    DocumentationStorage create(ZnaiServerConfig config);
}
