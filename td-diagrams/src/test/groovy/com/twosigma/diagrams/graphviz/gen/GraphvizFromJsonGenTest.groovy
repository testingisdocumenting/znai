package com.twosigma.diagrams.graphviz.gen

import org.junit.Test

/**
 * @author mykola
 */
class GraphvizFromJsonGenTest {
    private String gv

    @Test
    void "generates graph from nodes defined as json"() {
        generate([
                nodes: [
                        [id: "n1", label: "l1"],
                        [id: "n2", label: "l2"]],
                edges: [
                        ["n1", "n2"],
                        ["n1", "n2", "both"],
                ]])

        gv.should == "digraph Generated {\n" +
                "rankdir=LR;\n" +
                "bgcolor=\"#ffffff00\";\n" +
                "node [shape=record; fontsize=10; margin=0.2; fontname=Helvetica];\n" +
                "\n" +
                "n1 [label=\"l1\"];\n" +
                "n2 [label=\"l2\"];\n" +
                "\n" +
                "n1 -> n2;\n" +
                "n1 -> n2[dir=both];\n" +
                "}"
    }

    @Test
    void "applies meta information for highlighted items"() {
        generate([nodes: [[id: "n1", label: "l1", highlight: true]]])

        gv.should == "digraph Generated {\n" +
                "rankdir=LR;\n" +
                "bgcolor=\"#ffffff00\";\n" +
                "node [shape=record; fontsize=10; margin=0.2; fontname=Helvetica];\n" +
                "\n" +
                "n1 [label=\"l1[h]\"];\n" +
                "}"
    }

    private void generate(data) {
        def gen = new GraphvizFromJsonGen(data, false)
        gv = gen.generate()
    }
}
