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

package com.twosigma.znai.extensions.xml;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.Map;
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
        props.put("xmlAsJson", XmlToMapRepresentationConverter.convert(xml));
        props.put("paths", pluginParams.getOpts().getList("paths"));

        return PluginResult.docElement("Xml", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(componentsRegistry.resourceResolver().fullPath(fileName)));
    }
}
