package com.twosigma.documentation.extensions;

import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.include.IncludeResult;

/**
 * @author mykola
 */
public interface PluginsListener {
    void onReset(IncludeContext context);
    void onInclude(IncludePlugin plugin, IncludeResult result);
}
