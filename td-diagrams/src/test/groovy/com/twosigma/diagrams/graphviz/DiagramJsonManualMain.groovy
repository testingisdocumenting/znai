package com.twosigma.diagrams.graphviz

import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig
import com.twosigma.utils.JsonUtils
import com.twosigma.utils.ResourceUtils

import java.nio.file.Files
import java.nio.file.Paths

/**
 * manual testing of diagram JSON creation for UI consumption
 * @author mykola
 */
class DiagramJsonManualMain {
    static shapeConfig = new GraphvizShapeConfig(ResourceUtils.textContent("graphviz-meta-conf.json"))
    static runtime = new InteractiveCmdGraphviz()

    static void main(String[] args) {
        def engine = new GraphvizEngine(runtime, shapeConfig)

        def diagram = engine.diagramFromGv("""digraph Simple {
node[shape=record];
graph [nodesep=1];
    human [label="human [man a]"];
    world [label="[world c]"];
    server [label="server [a]"];
    another [label="another [b]"];

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
        Files.write(Paths.get("/Users/mykola/work/testing-documenting/td-documentation-reactjs/src/doc-elements/DiagramTestData.js"), export.bytes)
    }
}