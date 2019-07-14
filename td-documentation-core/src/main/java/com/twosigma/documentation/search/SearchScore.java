package com.twosigma.documentation.search;

/**
 * how likely text will be found by a search
 */
public enum SearchScore {
    HIGHEST,
    HIGHER,
    HIGH,
    STANDARD,
    LOW,
    LOWEST;

    public SearchText text(String text) {
        return new SearchText(text, this);
    }
}
