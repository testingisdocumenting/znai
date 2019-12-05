package com.twosigma.znai.server.support;

public interface SupportMetaGetter {
    SupportMeta get(String docId, String actor);
}
