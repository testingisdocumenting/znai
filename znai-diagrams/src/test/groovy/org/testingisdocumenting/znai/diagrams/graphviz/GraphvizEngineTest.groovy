/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.diagrams.graphviz

import org.testingisdocumenting.znai.utils.ResourceUtils
import org.testingisdocumenting.znai.diagrams.graphviz.meta.GraphvizShapeConfig
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.contain

class GraphvizEngineTest {
    static shapeConfig = new GraphvizShapeConfig(ResourceUtils.textContent("graphviz-meta-conf.json"))
    static dot = new InteractiveCmdGraphviz("dot")

    @Test
    void "should provide svg, styles and custom shapes as result"() {
        def engine = new GraphvizEngine(shapeConfig).registerRuntime(dot)

         def diagram = engine.diagramFromGv("dot", "id", """digraph Simple {
    "main-bridge" [label="mn [world]"];
    server [label="server\\nline [a]"];

    server -> "main-bridge";
}""")

        diagram.svg.should contain("server</text")
        diagram.svg.should contain("line</text")
        diagram.stylesByNodeId.should == ['main-bridge': ['world'], server: ['a']]
    }
}
