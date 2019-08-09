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

package com.twosigma.znai.diagrams.graphviz

import com.twosigma.znai.utils.FileUtils
import com.twosigma.znai.utils.JsonUtils

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
            next [label="preference[document]"];
    
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
        Files.write(Paths.get("znai-reactjs/src/doc-elements/graphviz/DiagramTestData.js"), export.bytes)
        FileUtils.writeTextContent(Paths.get("znai-reactjs/src/doc-elements/graphviz/DiagramTestData.svg"), diagram.getSvg())
    }
}