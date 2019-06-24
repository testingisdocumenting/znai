package com.twosigma.documentation.diagrams.graphviz

import com.twosigma.utils.FileUtils
import com.twosigma.utils.JsonUtils

import java.nio.file.Files
import java.nio.file.Paths

class GraphvizShapesTestManual {
    static void main(String[] args) {
        def diagram = Graphviz.graphvizEngine.diagramFromGv("dot", "test-id", """digraph Simple {
            rankdir=LR;
            node [shape=rectangle; fontsize=10; margin=0.2; fontname=Helvetica;];
    
            human [label="human [actor a]"];
            world [label="[world c]"];
            server [label="server [a]"];
            another [label="hello\\\\nworld of[b database]"];
    
            server -> another;
            server -> test -> human;
            another -> world;
            test -> another;
            human -> next;
            human -> world;
    
            {rank=same; human world;}
        }""")

        def json = JsonUtils.serialize(diagram.toMap())
        println json

        def export = "export default " + json
        Files.write(Paths.get("td-documentation-reactjs/src/doc-elements/graphviz/DiagramTestData.js"), export.bytes)
        FileUtils.writeTextContent(Paths.get("td-documentation-reactjs/src/doc-elements/graphviz/DiagramTestData.svg"), diagram.getSvg())
    }
}