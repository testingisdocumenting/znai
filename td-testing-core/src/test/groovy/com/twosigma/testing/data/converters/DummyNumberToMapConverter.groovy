package com.twosigma.testing.data.converters

/**
 * @author mykola
 */
class DummyNumberToMapConverter implements ToMapConverter {
    @Override
    Map<String, ?> convert(Object v) {
        return v instanceof Number ? Collections.singletonMap("DummyNumberToMapConverter", v) : null
    }
}
