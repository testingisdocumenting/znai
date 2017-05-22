package com.twosigma.testing.data.render;

import com.twosigma.testing.data.table.TableData;
import com.twosigma.testing.data.table.render.DefaultTableRenderStyle;
import com.twosigma.testing.data.table.render.TableRenderer;

/**
 * @author mykola
 */
public class TableDataRenderer
    implements DataRenderer {
    private static final DefaultTableRenderStyle renderStyle = new DefaultTableRenderStyle();

    @Override
    public String render(final Object data) {
        return (data instanceof TableData)
                ? "\n" + TableRenderer.render((TableData) data, renderStyle)
                : null;

//        if (! (data instanceof TableData)) {
//            return null;
//        }
//
//        return "\n" + TableRenderer.render((TableData) data, renderStyle);
    }
}
