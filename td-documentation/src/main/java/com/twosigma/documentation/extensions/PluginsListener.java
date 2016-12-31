package com.twosigma.documentation.extensions;

/**
 * @author mykola
 */
public interface PluginsListener {
    void onReset(IncludeContext context);
    void onInclude(IncludePlugin plugin, IncludeResult result);
}
