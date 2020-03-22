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

package org.testingisdocumenting.znai.diagrams.graphviz;

import org.testingisdocumenting.znai.diagrams.graphviz.meta.GraphvizDiagramWithMeta;
import org.testingisdocumenting.znai.diagrams.graphviz.meta.GraphvizShapeConfig;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class GraphvizEngine {
    public static final String DOT_LAYOUT = "dot";

    private final GraphvizShapeConfig shapeConfig;
    private final Map<String, GraphvizRuntime> runtimeByType;

    public GraphvizEngine(GraphvizShapeConfig shapeConfig) {
        this.shapeConfig = shapeConfig;
        this.runtimeByType = new HashMap<>();
    }

    public GraphvizDiagram diagramFromGv(String layoutType, String id, String gv) {
        GraphvizRuntime runtime = findGraphvizRuntime(layoutType);

        GraphvizDiagramWithMeta withMeta = GraphvizDiagramWithMeta.create(shapeConfig, gv);
        String graphSvg = runtime.svgFromGv(withMeta.getPreprocessed());

        Set<String> styles = withMeta.getStylesById().values().stream().flatMap(Collection::stream).collect(toSet());
        Map<String, Boolean> isInvertedTextColorByStyleId = new LinkedHashMap<>();
        styles.forEach((s) -> {
            boolean isInverted = shapeConfig.isInvertedTextColorByStyleId(s);
            if (isInverted) {
                isInvertedTextColorByStyleId.put(s, isInverted);
            }
        });

        return new GraphvizDiagram(id, graphSvg, withMeta.getStylesById(), isInvertedTextColorByStyleId);
    }

    private GraphvizRuntime findGraphvizRuntime(String layoutType) {
        GraphvizRuntime runtime = runtimeByType.get(layoutType);
        if (runtime == null) {
            throw new RuntimeException("no runtime found for layout type <" + layoutType + ">");
        }
        return runtime;
    }

    public GraphvizEngine registerRuntime(GraphvizRuntime runtime) {
        runtimeByType.put(runtime.getLayoutType(), runtime);
        return this;
    }
}
