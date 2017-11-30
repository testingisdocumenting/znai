package com.twosigma.documentation.search;

/**
 * @author mykola
 */
public class SearchText {
    private String text;
    private SearchScore score;

    public SearchText(String text, SearchScore score) {
        this.text = text;
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public SearchScore getScore() {
        return score;
    }
}
