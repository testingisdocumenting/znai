package com.twosigma.testing.data.render;

/**
 * @author mykola
 */
public class NullRenderer implements DataRenderer {
    @Override
    public String render(Object data) {
        return data == null ? "[null]" : null;
    }
}
