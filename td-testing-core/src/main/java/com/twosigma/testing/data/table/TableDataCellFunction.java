package com.twosigma.testing.data.table;

/**
 * @author mykola
 */
public interface TableDataCellFunction<T, R> {
    R apply(int rowIdx, int colIdx, String columnName, T v);
}
