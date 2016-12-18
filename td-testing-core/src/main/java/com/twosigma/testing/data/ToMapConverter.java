package com.twosigma.testing.data;

import java.util.Map;

/**
 * @author mykola
 */
public interface ToMapConverter {
    Map<String, ?> convert(Object v);
}
