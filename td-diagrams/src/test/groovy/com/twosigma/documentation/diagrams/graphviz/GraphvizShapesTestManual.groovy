package com.twosigma.documentation.diagrams.graphviz

import com.twosigma.utils.JsonUtils

import java.nio.file.Files
import java.nio.file.Paths

class GraphvizShapesTestManual {
    static void main(String[] args) {
        def diagram = Graphviz.graphvizEngine.diagramFromGv("dot", "test-id", """digraph Simple {
            node[shape=record];
            node [shape=record; fontsize=10; margin=0.2; fontname=Helvetica];
    
            human [label="human [man a]"];
            world [label="[world c]"];
            server [label="server [a]"];
            another [label="another[b database]"];
    
            server -> test -> human;
            server -> another;
            another -> world;
            human -> next;
            human -> world;
    
            {rank=same; human world;}
        }""")

        def json = JsonUtils.serialize(diagram.toMap())
        println json

        def export = "export default " + json
        Files.write(Paths.get("td-documentation-reactjs/src/doc-elements/graphviz/DiagramTestData.js"), export.bytes)
    }
}