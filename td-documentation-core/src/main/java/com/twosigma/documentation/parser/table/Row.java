package com.twosigma.documentation.parser.table;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
public class Row {
    private List<Object> data;

    public Row() {
        data = new ArrayList<>();
    }

    public void add(Object v) {
        data.add(v);
    }

    public List<Object> getData() {
        return data;
    }
}
