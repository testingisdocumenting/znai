/*
 * Copyright 2022 znai maintainers
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

import org.testingisdocumenting.znai.core.GlobalAssetsRegistry;
import org.testingisdocumenting.znai.diagrams.DiagramsGlobalAssetsRegistration;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

class GraphvizPlugin {
    public static PluginResult pluginResult(GlobalAssetsRegistry globalAssetsRegistry,
                                            PluginParams pluginParams,
                                            String gvContent) {
        String diagramId = generateId();

        DiagramsGlobalAssetsRegistration.register(globalAssetsRegistry);

        GraphvizDiagram diagram = Graphviz.graphvizEngine.diagramFromGv(
                pluginParams.getOpts().get("type", GraphvizEngine.DOT_LAYOUT),
                diagramId, gvContent);

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());
        props.put("urls", Collections.emptyList());

        return PluginResult.docElement("GraphVizDiagram", props);

    }

    private static String generateId() {
        return "gv_" + UUID.randomUUID().toString().replaceAll("-", "_");
    }
}
