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

         def diagram = engine.diagramFromGv("""digraph Simple {
    main [label="mn [database]"];
    server [label="server [a]"];

    server -> main;
}""")

        assert diagram.stylesByNodeId == [main: ['database'], server: ['a']]
        assert diagram.shapeSvgByStyleId == [database: '<svg><g></g></svg>']
    }
}
