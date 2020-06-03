package org.testingisdocumenting.znai.enterprise.storage;

import org.testingisdocumenting.znai.server.ZnaiServerConfig;
import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.util.Set;

public class DocumentationStorageFactories {
    private static final Set<DocumentationStorageFactory> handlers =
            ServiceLoaderUtils.load(DocumentationStorageFactory.class);

    public static DocumentationStorage create(ZnaiServerConfig config) {
        return handlers.stream().findFirst().map(h -> h.create(config)).orElse(null);
    }

    public static void add(DocumentationStorageFactory handler) {
        handlers.add(handler);
    }

}
