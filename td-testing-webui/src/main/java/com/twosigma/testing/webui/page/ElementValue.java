package com.twosigma.testing.webui.page;

/**
 * @author mykola
 */
public class ElementValue<E> {
    private String name;
    private ElementValueFetcher<E> valueFetcher;

    public ElementValue(String name, ElementValueFetcher<E> valueFetcher) {
        this.name = name;
        this.valueFetcher = valueFetcher;
    }

    public String getName() {
        return name;
    }

    public E get() {
        return valueFetcher.fetch();
    }
}
