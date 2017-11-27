package com.twosigma.documentation.parser.table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class Row {
    private List<Object> data;

    public Row() {
        data = new ArrayList<>();
    }

    Row(List<Object> data) {
        this.data = data;
    }

    public void add(Object v) {
        data.add(v);
    }

    public List<Object> getData() {
        return data;
    }

    public Row onlyWithIdxs(List<Integer> idxs) {
        return new Row(idxs.stream().map(data::get).collect(toList()));
    }
}
