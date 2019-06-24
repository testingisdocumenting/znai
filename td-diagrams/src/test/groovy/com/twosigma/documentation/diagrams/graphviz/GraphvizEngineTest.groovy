package com.twosigma.documentation.diagrams.graphviz

import com.twosigma.documentation.diagrams.graphviz.meta.GraphvizShapeConfig
import com.twosigma.utils.ResourceUtils
import org.junit.Test

/**
 * @author mykola
 */
class GraphvizEngineTest {
    static shapeConfig = new GraphvizShapeConfig(ResourceUtils.textContent("graphviz-meta-conf.json"))
    static dot = new InteractiveCmdGraphviz("dot")

    @Test
    void "should provide svg, styles and custom shapes as result"() {
        def engine = new GraphvizEngine(shapeConfig).registerRuntime(dot)

         def diagram = engine.diagramFromGv("dot", "id", """digraph Simple {
    main [label="mn [world]"];
    server [label="server [a]"];

    server -> main;
}""")

        assert diagram.stylesByNodeId == [main: ['world'], server: ['a']]
    }
}
