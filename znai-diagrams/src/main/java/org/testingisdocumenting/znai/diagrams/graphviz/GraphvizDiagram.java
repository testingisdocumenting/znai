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

package com.twosigma.znai.diagrams.graphviz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphvizDiagram {
    private String id;
    private String svg;
    private Map<String, List<String>> stylesByNodeId;
    private Map<String, Boolean> isInvertedTextColorByStyleId;

    public GraphvizDiagram(String id, String svg, Map<String, List<String>> stylesByNodeId,
                           Map<String, Boolean> isInvertedTextColorByStyleId) {
        this.id = id;
        this.svg = svg;
        this.stylesByNodeId = stylesByNodeId;
        this.isInvertedTextColorByStyleId = isInvertedTextColorByStyleId;
    }

    public String getSvg() {
        return svg;
    }

    public Map<String, List<String>> getStylesByNodeId() {
        return stylesByNodeId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("svg", svg);
        result.put("stylesByNodeId", stylesByNodeId);
        result.put("isInvertedTextColorByStyleId", isInvertedTextColorByStyleId);

        return result;
    }
}
