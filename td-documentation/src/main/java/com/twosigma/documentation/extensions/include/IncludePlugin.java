package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.ComponentsRegistry;
import com.twosigma.documentation.extensions.ReactComponent;

/**
 *
 * @author mykola
 */
public interface IncludePlugin {
    String id();

    default void init(ComponentsRegistry documentationComponentsRegistry) {

    }

    /**
     * gets called at the beginning of every page before rendering
     * @param context context of the page
     */
    default void reset(IncludeContext context) {}

    ReactComponent process(ComponentsRegistry componentsRegistry, IncludeParams includeParams);
    String textForSearch(); // TODO weights
}
