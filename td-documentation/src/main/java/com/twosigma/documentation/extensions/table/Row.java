package com.twosigma.documentation.extensions.table;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
class Row {
    private List<Object> data;

    Row() {
        data = new ArrayList<>();
    }

    public void add(Object v) {
        data.add(v);
    }

    public List<Object> getData() {
        return data;
    }
}
