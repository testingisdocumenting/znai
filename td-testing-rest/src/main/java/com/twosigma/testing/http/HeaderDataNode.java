package com.twosigma.testing.http;

import java.util.List;
import java.util.Map;

import com.twosigma.testing.data.traceable.TraceableValue;
import com.twosigma.testing.http.datanode.DataNode;
import com.twosigma.testing.http.datanode.DataNodeId;

/**
 * @author mykola
 */
public class HeaderDataNode implements DataNode {
    private DataNode dataNode;

    public HeaderDataNode(DataNode dataNode) {
        this.dataNode = dataNode;
    }

    @Override
    public DataNodeId id() {
        return dataNode.id();
    }

    @Override
    public DataNode get(String name) {
        return dataNode.get(name);
    }

    @Override
    public DataNode get(int idx) {
        return dataNode.get(idx);
    }

    @Override
    public TraceableValue get() {
        return dataNode.get();
    }

    @Override
    public boolean isList() {
        return dataNode.isList();
    }

    @Override
    public boolean isSingleValue() {
        return false;
    }

    @Override
    public List<DataNode> all() {
        return dataNode.all();
    }

    @Override
    public int numberOfChildren() {
        return dataNode.numberOfChildren();
    }

    @Override
    public int numberOfElements() {
        return dataNode.numberOfElements();
    }

    @Override
    public Map<String, DataNode> asMap() {
        return dataNode.asMap();
    }

    @Override
    public String toString() {
        return dataNode.toString();
    }
}
