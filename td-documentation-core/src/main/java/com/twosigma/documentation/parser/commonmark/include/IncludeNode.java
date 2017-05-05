package com.twosigma.documentation.parser.commonmark.include;

import com.twosigma.documentation.extensions.PluginParams;
import org.commonmark.node.CustomBlock;
import org.commonmark.node.Visitor;

/**
 * @author mykola
 */
public class IncludeNode extends CustomBlock {
    private PluginParams params;

    public IncludeNode(PluginParams params) {
        this.params = params;
    }

    public String getId() {
        return params.getPluginId();
    }

    public PluginParams getParams() {
        return params;
    }

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }
}
