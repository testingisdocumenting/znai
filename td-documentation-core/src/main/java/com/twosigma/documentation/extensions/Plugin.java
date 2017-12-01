package com.twosigma.documentation.extensions;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.search.SearchText;

import java.util.stream.Stream;

/**
 * @author mykola
 */
public interface Plugin {
    String id();

    default Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.empty();
    }

    default SearchText textForSearch() {
        return null;
    }
}
