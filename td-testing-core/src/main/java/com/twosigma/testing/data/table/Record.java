package com.twosigma.testing.data.table;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author mykola
 * // TODO make iterable?
 */
public class Record {
    private Header header;
    private List<Object> values;

    public Record(Header header, Stream<Object> values) {
        this.header = header;
        this.values = values.collect(toList());
    }

    public Header header() {
        return header;
    }

    public Object valueByName(String name) {
        return values.get(header.columnIdxByName(name));
    }

    public Object valueByIdx(int idx) {
        header.validateIdx(idx);
        return values.get(idx);
    }

    public Stream<Object> values() {
        return values.stream();
    }

    @SuppressWarnings("unchecked")
    public  <T, R> Stream<R> mapValues(Function<T, R> mapper) {
        return values.stream().map(v -> mapper.apply((T) v));
    }

    public Map<String, Object> asMap() {
        return header.columnIdxStream().boxed().
            collect(toMap(
                idx -> header.columnNameByIdx(idx),
                idx -> values.get(idx)));
    }

    @Override
    public String toString() {
        return asMap().toString();
    }
}
