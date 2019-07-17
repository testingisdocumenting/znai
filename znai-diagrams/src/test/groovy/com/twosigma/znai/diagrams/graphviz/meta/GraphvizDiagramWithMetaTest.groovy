package com.twosigma.znai.diagrams.graphviz.meta

import org.junit.Assert
import org.junit.Test

class GraphvizDiagramWithMetaTest {
    static shapeConfig = new GraphvizShapeConfig([person: [shape: 'octagon', width: 1, height: 2, svgPath: "person.svg"]])

    @Test
    void "should extract styles based on labels"() {
        def diagram = GraphvizDiagramWithMeta.create(shapeConfig, """digraph Simple {
    main [label="mn [a b]"];
    server [label="server [a]"];

    main -> server;
}
""")
        assert diagram.stylesById == [main: ['a', 'b'], server: ['a']]
        Assert.assertEquals("digraph Simple {\n" +
                "    main [label=\"mn\"];\n" +
                "    server [label=\"server\"];\n" +
                "\n" +
                "    main -> server;\n" +
                "}", diagram.getPreprocessed())
    }

    @Test
    void "should add shape information for database style"() {
        def diagram = GraphvizDiagramWithMeta.create(shapeConfig, """main [label="mn [person a]"];""")

        Assert.assertEquals("main [label=\"mn\",shape=octagon; width=1; height=2; fixedsize=true];", diagram.getPreprocessed())
    }
}
