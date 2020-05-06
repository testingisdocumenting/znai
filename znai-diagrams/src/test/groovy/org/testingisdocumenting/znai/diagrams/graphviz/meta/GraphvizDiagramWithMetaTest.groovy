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

package org.testingisdocumenting.znai.diagrams.graphviz.meta

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
