package com.twosigma.documentation.extensions;

import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.include.IncludeResult;

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
