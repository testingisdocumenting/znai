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

package org.testingisdocumenting.znai.extensions.reactjs;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.utils.CollectionUtils;

import java.nio.file.Path;
import java.util.List;

public class ReactJsComponentIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "reactjs-component";
    }

    @Override
    public IncludePlugin create() {
        return new ReactJsComponentIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        ComponentPath componentPath = extractComponentPath(pluginParams.getFreeParam());

        return PluginResult.docElement("CustomReactJSComponent", CollectionUtils.createMap(
                "namespace", componentPath.namespace,
                "name", componentPath.name,
                "props", pluginParams.getOpts().toMap()));
    }

    private static ComponentPath extractComponentPath(String fullName) {
        String[] parts = fullName.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("component path must be specified as <namespace>.<name>," +
                    " given: " + fullName);
        }

        return new ComponentPath(parts[0], parts[1]);
    }

    private static class ComponentPath {
        private String namespace;
        private String name;

        ComponentPath(String namespace, String name) {
            this.namespace = namespace;
            this.name = name;
        }
    }

    @Override
    public List<SearchText> textForSearch() {
        // TODO implement textForSearch
        return List.of();
    }
}
