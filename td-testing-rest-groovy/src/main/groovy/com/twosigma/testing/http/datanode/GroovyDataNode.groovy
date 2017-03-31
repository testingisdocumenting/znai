package com.twosigma.testing.http.datanode

import com.twosigma.testing.data.traceable.TraceableValue
import com.twosigma.testing.expectation.Should

/**
 * @author mykola
 */
class GroovyDataNode implements DataNode {
    private DataNode node

    GroovyDataNode(final DataNode node) {
        this.node = node
    }

    def getProperty(String name) {
        if (name == "should") {
            return new Should(node)
        }

        return get(name)
    }

    def getAt(Integer idx) {
        return get(idx)
    }

    @Override
    DataNodeId id() {
        return node.id()
    }

    @Override
    DataNode get(final String name) {
        return new GroovyDataNode(node.get(name))
    }

    @Override
    DataNode get(final int idx) {
        return new GroovyDataNode(node.get(idx))
    }

    @Override
    TraceableValue get() {
        return node.get()
    }

    @Override
    boolean isList() {
        return node.isList()
    }

    @Override
    boolean isSingleValue() {
        return node.isSingleValue()
    }

    @Override
    List<DataNode> all() {
        return node.all().collect { new GroovyDataNode(it) }
    }

    @Override
    int numberOfChildren() {
        return node.numberOfChildren()
    }

    @Override
    int numberOfElements() {
        return node.numberOfElements()
    }

    @Override
    Map<String, DataNode> asMap() {
        return node.asMap().entrySet().collectEntries { [it.key, new GroovyDataNode(it.value)] }
    }

    @Override
    String toString() {
        return node.toString()
    }
}
