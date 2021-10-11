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

package org.testingisdocumenting.znai.diagrams.graphviz;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.diagrams.DiagramsGlobalAssetsRegistration;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class GraphvizIncludePlugin implements IncludePlugin {
    private Path diagramPath;

    @Override
    public String id() {
        return "graphviz";
    }

    @Override
    public IncludePlugin create() {
        return new GraphvizIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        String diagramId = generateId();
        diagramPath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        String gvContent = componentsRegistry.resourceResolver().textContent(diagramPath);

        DiagramsGlobalAssetsRegistration.register(componentsRegistry.globalAssetsRegistry());

        GraphvizDiagram diagram = Graphviz.graphvizEngine.diagramFromGv(
                pluginParams.getOpts().get("type", GraphvizEngine.DOT_LAYOUT),
                diagramId, gvContent);
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());
        props.put("urls", Collections.emptyList());

        return PluginResult.docElement("GraphVizDiagram", props);
    }

    private String generateId() {
        return "gv_" + UUID.randomUUID().toString().replaceAll("-", "_");
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(diagramPath));
    }
}
