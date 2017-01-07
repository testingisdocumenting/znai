package com.twosigma.graphviz.meta

import org.junit.Assert
import org.junit.Test

/**
 * @author mykola
 */
class GraphvizDiagramWithMetaTest {
    @Test
    void "should extract styles based on labels"() {
        def diagram = GraphvizDiagramWithMeta.create("""digraph Simple {
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
        def diagram = GraphvizDiagramWithMeta.create("""main [label="mn [database a]"];""")

        Assert.assertEquals("main [label=\"mn\",shape=\"octagon\",width=1,height=2,fixedSize=true];", diagram.getPreprocessed())
    }
}
