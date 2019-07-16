package com.twosigma.documentation.extensions;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.search.SearchText;

import java.util.stream.Stream;

public interface Plugin {
    String id();

    Plugin create();

    default Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.empty();
    }

    default SearchText textForSearch() {
        return null;
    }
}
