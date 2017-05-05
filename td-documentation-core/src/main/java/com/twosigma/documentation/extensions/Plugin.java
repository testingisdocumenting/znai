package com.twosigma.documentation.extensions;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;

import java.util.stream.Stream;

/**
 * @author mykola
 */
public interface Plugin {
    String id();

    default Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.empty();
    }

    default String textForSearch() {
        return "";
    } // TODO weights
}
