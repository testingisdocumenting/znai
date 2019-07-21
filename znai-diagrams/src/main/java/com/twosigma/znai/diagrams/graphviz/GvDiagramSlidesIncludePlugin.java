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

import com.twosigma.znai.diagrams.DiagramsGlobalAssetsRegistration;
import com.twosigma.znai.diagrams.slides.DiagramSlides;
import com.twosigma.znai.diagrams.slides.MarkupDiagramSlides;
import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.utils.NameUtils;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GvDiagramSlidesIncludePlugin implements IncludePlugin {
    private List<AuxiliaryFile> auxiliaryFiles;
    private Path diagramPath;
    private Path slidesPath;

    @Override
    public String id() {
        return "gv-diagram-slides";
    }

    @Override
    public IncludePlugin create() {
        return new GvDiagramSlidesIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        MarkupParser parser = componentsRegistry.defaultParser();
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();

        DiagramsGlobalAssetsRegistration.register(componentsRegistry.globalAssetsRegistry());

        String diagramTitle = pluginParams.getFreeParam();
        String diagramId = NameUtils.idFromTitle(diagramTitle);

        diagramPath = resourcesResolver.fullPath(pluginParams.getOpts().getRequiredString("diagramPath"));
        slidesPath = resourcesResolver.fullPath(pluginParams.getOpts().getRequiredString("slidesPath"));

        String gvContent = resourcesResolver.textContent(diagramPath);
        String slidesContent = resourcesResolver.textContent(slidesPath);

        MarkupDiagramSlides markupSlides = new MarkupDiagramSlides(parser);
        DiagramSlides diagramSlides = markupSlides.create(markupPath, slidesContent);

        auxiliaryFiles = markupSlides.getAuxiliaryFiles();

        Map<String, Object> props = new LinkedHashMap<>();

        props.put("slides", diagramSlides.toListOfMaps());
        props.put("diagram", Graphviz.graphvizEngine.diagramFromGv(
                pluginParams.getOpts().get("type", GraphvizEngine.DOT_LAYOUT),
                diagramId, gvContent).toMap());

        return PluginResult.docElement("GraphVizFlow", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(auxiliaryFiles.stream(), Stream.of(
                AuxiliaryFile.builtTime(diagramPath),
                AuxiliaryFile.builtTime(slidesPath)));
    }
}
