package com.twosigma.documentation.search;

/**
 * how likely text will be found by a search
 * @author mykola
 */
public enum SearchScore {
    HIGHEST,
    HIGHER,
    HIGH,
    STANDARD,
    LOW,
    LOWEST;

    SearchText text(String text) {
        return new SearchText(text, this);
    }
}
