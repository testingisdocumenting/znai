package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.AuxiliaryFile;
import com.twosigma.documentation.ComponentsRegistry;
import com.twosigma.documentation.extensions.ReactComponent;

import java.nio.file.Path;
import java.util.stream.Stream;

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
    default void reset(IncludeContext context) {}

    ReactComponent process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams);

    default Stream<AuxiliaryFile> filesPluginDependsOn(ComponentsRegistry componentsRegistry, IncludeParams includeParams) {
        return Stream.empty();
    }

    default String textForSearch() {
        return "";
    } // TODO weights
}
