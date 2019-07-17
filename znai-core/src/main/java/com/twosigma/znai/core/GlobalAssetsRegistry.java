package com.twosigma.znai.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalAssetsRegistry {
    private Map<String, Object> assets;

    public GlobalAssetsRegistry() {
        assets = new ConcurrentHashMap<>();
    }

    public void updateAsset(String key, Object value) {
        assets.put(key, value);
    }

    public Map<String, Object> getAssets() {
        return assets;
    }
}
