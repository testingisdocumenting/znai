package com.twosigma.testing.http.datanode;

/**
 * @author mykola
 */
public class DataNodeId {
    private String path;
    private String name;
    private int idx;

    public DataNodeId(final String name) {
        this.name = name;
        this.path = name;
    }

    public DataNodeId(final String path, final String name) {
        this.path = path;
        this.name = name;
    }

    public DataNodeId(final String path, final String name, int idx) {
        this(path, name);
        this.idx = idx;
    }

    public DataNodeId child(final String name) {
        return new DataNodeId(path + "." + name, name);
    }

    public DataNodeId peer(final int idx) {
        return new DataNodeId(path + "[" + idx + "]", name, idx);
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public int getIdx() {
        return idx;
    }

    @Override
    public String toString() {
        return path;
    }
}
