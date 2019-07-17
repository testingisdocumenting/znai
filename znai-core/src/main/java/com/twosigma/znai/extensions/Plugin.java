package com.twosigma.znai.extensions;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.search.SearchText;

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
