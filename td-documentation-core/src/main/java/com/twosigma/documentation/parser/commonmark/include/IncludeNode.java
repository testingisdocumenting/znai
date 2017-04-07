package com.twosigma.documentation.parser.commonmark.include;

import com.twosigma.documentation.extensions.include.IncludeParams;
import org.commonmark.node.CustomBlock;
import org.commonmark.node.Visitor;

/**
 * @author mykola
 */
public class IncludeNode extends CustomBlock {
    private IncludeParams params;

    public IncludeNode(IncludeParams params) {
        this.params = params;
    }

    public String getId() {
        return params.getPluginId();
    }

    public IncludeParams getParams() {
        return params;
    }

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }
}
