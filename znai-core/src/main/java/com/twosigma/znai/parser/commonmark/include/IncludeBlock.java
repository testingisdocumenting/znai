package com.twosigma.znai.parser.commonmark.include;

import com.twosigma.znai.extensions.PluginParams;
import org.commonmark.node.CustomBlock;
import org.commonmark.node.Visitor;

public class IncludeBlock extends CustomBlock {
    private PluginParams params;

    IncludeBlock() {
    }

    public void setParams(PluginParams params) {
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
