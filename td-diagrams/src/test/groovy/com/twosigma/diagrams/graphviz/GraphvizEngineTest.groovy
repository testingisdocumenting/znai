package com.twosigma.diagrams.graphviz

import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig
import com.twosigma.utils.ResourceUtils
import org.junit.Test

/**
 * @author mykola
 */
class GraphvizEngineTest {
    static shapeConfig = new GraphvizShapeConfig(ResourceUtils.textContent("graphviz-meta-conf.json"))
    static runtime = new InteractiveCmdGraphviz()

    @Test
    void "should provide svg, styles and custom shapes as result"() {
        def engine = new GraphvizEngine(runtime, shapeConfig)

         def diagram = engine.diagramFromGv("id", """digraph Simple {
    main [label="mn [world]"];
    server [label="server [a]"];

    server -> main;
}""")

        assert diagram.stylesByNodeId == [main: ['world'], server: ['a']]
        assert ! diagram.shapeSvgByStyleId.isEmpty()
        assert diagram.shapeSvgByStyleId.world =~ ~/transform/
    }
}
