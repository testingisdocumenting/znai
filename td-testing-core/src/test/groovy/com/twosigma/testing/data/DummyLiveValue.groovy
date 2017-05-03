package com.twosigma.testing.data

import com.twosigma.testing.data.live.LiveValue

/**
 * @author mykola
 */
class DummyLiveValue implements LiveValue {
    int index
    List<?> values

    DummyLiveValue(List<?> values) {
        this.values = values
    }

    @Override
    Object get() {
        return values[index++]
    }
}
