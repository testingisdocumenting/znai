package com.twosigma.documentation.search;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author mykola
 */
@XmlRootElement
public class SearchText {
    private String text;
    private SearchScore score;

    public SearchText() {
    }

    public SearchText(String text, SearchScore score) {
        this.text = text;
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SearchScore getScore() {
        return score;
    }

    public void setScore(SearchScore score) {
        this.score = score;
    }
}
