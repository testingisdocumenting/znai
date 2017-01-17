package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.extensions.ReactComponent;

/**
 *
 * @author mykola
 */
public interface IncludePlugin {
    String id();

    /**
     * gets called at the beginning of every page before rendering
     * @param context context of the page
     */
    void reset(IncludeContext context);

    ReactComponent process(IncludeResourcesResolver resourcesResolver, IncludeParams includeParams);
    String textForSearch(); // TODO weights
}
