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

package com.twosigma.znai.extensions.reactjs;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.utils.CollectionUtils;

import java.nio.file.Path;

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
}
