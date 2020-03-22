/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.diagrams.graphviz.gen

import org.junit.Test

import static com.twosigma.webtau.Matchers.contain

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
    void "generates graphviz compatible line break for multiline labels"() {
        generate([
                nodes: [[id: "n", label: "multi\nline"]],
                edges: [["n", "n"]
                ]])

        gv.should contain("n [label=\"multi\\nline\"]")
    }

    @Test
    void "pass size information to each node from config section"() {
        generate([
                nodes: [
                        [id: "n1", label: "l1"],
                        [id: "n2", label: "l2"]],
                edges: [
                        ["n1", "n2"],
                        ["n1", "n2"]],
                config: [
                        nodes: [
                            width: 4,
                            height: 2
                        ]]
                ])

        gv.should contain("n1 [label=\"l1\" fixedsize=true width=4 height=2]")
        gv.should contain("n2 [label=\"l2\" fixedsize=true width=4 height=2]")
    }

    @Test
    void "overrides config size by node size"() {
        generate([
                nodes: [
                        [id: "n1", label: "l1"],
                        [id: "n2", label: "l2", width: 8, height: 1]],
                edges: [
                        ["n1", "n2"],
                        ["n1", "n2"]],
                config: [
                        nodes: [
                            width: 4,
                            height: 2
                        ]]
                ])

        gv.should contain("n1 [label=\"l1\" fixedsize=true width=4 height=2]")
        gv.should contain("n2 [label=\"l2\" fixedsize=true width=8 height=1]")
    }

    @Test
    void "applies meta information for highlighted items"() {
        generate([nodes: [[id: "n", label: "l", highlight: true]], edges: [["n", "n"]]])
        gv.should contain("n [label=\"l[h]\"]")
    }

    @Test
    void "applies meta information when color group is specified"() {
        generate([nodes: [[id: "n", label: "l", colorGroup: "b"]], edges: [["n", "n"]]])
        gv.should contain("n [label=\"l[b]\"]")
    }

    @Test
    void "applies meta information when shape is specified"() {
        generate([nodes: [[id: "n", label: "l", shape: "database"]], edges: [["n", "n"]]])
        gv.should contain("n [label=\"l[database]\"]")
    }

    @Test
    void "applies meta information when shape and color is specified"() {
        generate([nodes: [[id: "n", label: "l", colorGroup: "b", shape: "database"]], edges: [["n", "n"]]])
        gv.should contain("n [label=\"l[b database]\"]")
    }

    @Test
    void "applies meta information when shape and highlight is specified"() {
        generate([nodes: [[id: "n", label: "l", highlight: true, shape: "database"]], edges: [["n", "n"]]])
        gv.should contain("n [label=\"l[h database]\"]")
    }

    @Test
    void "allows to specify nodes libraries to use and only register referenced nodes"() {
        generate([edges: [["a", "b"], ["c", "d"]]], [
                [
                        [id: "a", label: "A"],
                        [id: "b", label: "B"],
                ],
                [
                        [id: "c", label: "C"],
                        [id: "e", label: "E"],
                ],
        ])

        gv.should == 'digraph Generated {\n' +
                'rankdir=LR;\n' +
                'bgcolor="#ffffff00";\n' +
                'node [shape=record; fontsize=10; margin=0.2; fontname=Helvetica];\n' +
                '\n' +
                'a [label="A"];\n' +
                'b [label="B"];\n' +
                'c [label="C"];\n' +
                '\n' +
                'a -> b;\n' +
                'c -> d;\n' +
                '}'
    }

    private void generate(data, nodesLibraries = []) {
        def gen = new GraphvizFromJsonGen(data, nodesLibraries, new GraphvizGenConfig(isVertical: false))
        gv = gen.generate().graphViz
    }
}
