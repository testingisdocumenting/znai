package com.twosigma.testing.data.table;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    public Map<String, Object> toMap() {
        return header.columnIdxStream().boxed().
                collect(Collectors.toMap(
                        idx -> header.columnNameByIdx(idx),
                        idx -> values.get(idx),
                        (v1,v2) -> { throw new RuntimeException(String.format("duplicate key for values %s and %s", v1, v2)); },
                        LinkedHashMap::new));
    }

    @Override
    public String toString() {
        return toMap().toString();
    }
}
