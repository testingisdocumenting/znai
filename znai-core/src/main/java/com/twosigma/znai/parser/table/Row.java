package com.twosigma.znai.parser.table;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    @SuppressWarnings("unchecked")
    public <E> E get(int idx) {
        return (E) data.get(idx);
    }

    public Row onlyWithIdxs(List<Integer> idxs) {
        return new Row(idxs.stream().map(data::get).collect(toList()));
    }
}
