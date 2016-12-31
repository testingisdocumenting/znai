package com.twosigma.documentation.parser.commonmark.include;

import org.commonmark.node.CustomBlock;
import org.commonmark.node.Visitor;

/**
 * @author mykola
 */
public class IncludeNode extends CustomBlock {
    private String id;
    private String value;

    public IncludeNode(final String id, final String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }
}
