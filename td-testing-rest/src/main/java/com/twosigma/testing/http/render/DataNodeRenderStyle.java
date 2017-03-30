package com.twosigma.testing.http.render;

import com.twosigma.testing.data.traceable.CheckLevel;

/**
 * @author mykola
 */
public interface DataNodeRenderStyle {
    String wrapChecked(CheckLevel checkLevel, Object value);
}
