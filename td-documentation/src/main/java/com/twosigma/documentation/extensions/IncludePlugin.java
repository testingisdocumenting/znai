package com.twosigma.documentation.extensions;

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

    ReactComponent process(IncludeParams includeParams);
    String textForSearch(); // TODO weights
}
