package com.twosigma.documentation.extensions;

/**
 * @author mykola
 */
public class NoOpPluginListener implements PluginsListener {
    @Override
    public void onReset(final IncludeContext context) {
    }

    @Override
    public void onInclude(final IncludePlugin plugin, final IncludeResult result) {
    }
}
