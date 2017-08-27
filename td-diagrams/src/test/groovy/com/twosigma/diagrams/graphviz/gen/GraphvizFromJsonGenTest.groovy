package com.twosigma.diagrams.graphviz.gen

import com.twosigma.utils.JsonUtils
import org.junit.Test

/**
 * @author mykola
 */
class GraphvizFromJsonGenTest {
    @Test
    void "generates graph from nodes defined as json"() {
        def gen = new GraphvizFromJsonGen()
        def gv = gen.generate(JsonUtils.serializePrettyPrint([
                nodes: [
                        [id: "n1", label: "l1"],
                        [id: "n2", label: "l2"]],
                edges: [
                        ["n1", "n2"],
                        ["n1", "n2", "both"],
                ]
        ]))

        gv.should == "digraph Generated {\n" +
                "node [shape=record];\n" +
                "graph [nodesep=1];\n" +
                "\n" +
                "n1 [label=\"l1\"];\n" +
                "n2 [label=\"l2\"];\n" +
                "\n" +
                "n1 -> n2;\n" +
                "n1 -> n2[dir=both];\n" +
                "}"
    }
}
