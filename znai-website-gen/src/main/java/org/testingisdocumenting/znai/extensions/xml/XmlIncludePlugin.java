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

package org.testingisdocumenting.znai.extensions.xml;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.extensions.validation.EntryPresenceValidation;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class XmlIncludePlugin implements IncludePlugin {
    private String fileName;

    @Override
    public String id() {
        return "xml";
    }

    @Override
    public IncludePlugin create() {
        return new XmlIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        fileName = pluginParams.getFreeParam();
        String xml = componentsRegistry.resourceResolver().textContent(fileName);

        Map<String, Object> props = pluginParams.getOpts().toMap();

        Map<String, ?> xmlAsJson = XmlToMapRepresentationConverter.convert(xml);
        List<String> paths = pluginParams.getOpts().getList("paths");
        validatePaths(xmlAsJson, paths);

        props.put("xmlAsJson", xmlAsJson);
        props.put("paths", paths);

        return PluginResult.docElement("Xml", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(componentsRegistry.resourceResolver().fullPath(fileName)));
    }

    private static void validatePaths(Map<String, ?> xmlAsJson, List<String> paths) {
        Set<String> existingPaths = buildPaths(xmlAsJson);
        EntryPresenceValidation.validateItemsPresence("path", "XML", existingPaths, paths);
    }

    private static Set<String> buildPaths(Map<String, ?> xmlAsJson) {
        return new XmlPaths(xmlAsJson).getPaths();
    }
}
