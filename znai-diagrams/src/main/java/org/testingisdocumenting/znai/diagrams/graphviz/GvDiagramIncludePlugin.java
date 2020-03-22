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

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.diagrams.DiagramsGlobalAssetsRegistration;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class GvDiagramIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "gv-diagram";
    }

    @Override
    public IncludePlugin create() {
        return new GvDiagramIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        String diagramId = pluginParams.getFreeParam();
        String diagramPath = pluginParams.getOpts().getRequiredString("diagramPath");
        String gvContent = componentsRegistry.resourceResolver().textContent(diagramPath);

        DiagramsGlobalAssetsRegistration.register(componentsRegistry.globalAssetsRegistry());

        GraphvizDiagram diagram = Graphviz.graphvizEngine.diagramFromGv(
                pluginParams.getOpts().get("type", GraphvizEngine.DOT_LAYOUT),
                diagramId, gvContent);
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());

        return PluginResult.docElement("GraphVizDiagram", props);
    }
}
