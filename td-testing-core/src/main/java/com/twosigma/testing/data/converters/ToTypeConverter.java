package com.twosigma.testing.data.converters;

import java.util.Map;

/**
 * @author mykola
 */
public interface ToTypeConverter<E> {
    E convert(Object v);
}
