package com.twosigma.testing.webui.documentation;

import com.twosigma.testing.webui.page.PageElement;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mykola
 */
public class Annotation {
    private static AtomicInteger idGen = new AtomicInteger();

    private String id;
    private String type;
    private String text;
    private PageElement pageElement;

    public Annotation(String type, String text, PageElement pageElement) {
        this.id = type + idGen.incrementAndGet();
        this.type = type;
        this.text = text;
        this.pageElement = pageElement;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public PageElement getPageElement() {
        return pageElement;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
