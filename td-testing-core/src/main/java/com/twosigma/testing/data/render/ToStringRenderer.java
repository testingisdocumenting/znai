package com.twosigma.testing.data.render;

/**
 * @author mykola
 */
public class ToStringRenderer implements DataRenderer {
    @Override
    public String render(Object data) {
        return data.toString();
    }
}
