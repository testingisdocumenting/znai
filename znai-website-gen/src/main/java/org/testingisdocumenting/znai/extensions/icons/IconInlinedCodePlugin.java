/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.extensions.icons;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class IconInlinedCodePlugin implements InlinedCodePlugin {
    @Override
    public String id() {
        return "icon";
    }

    @Override
    public InlinedCodePlugin create() {
        return new IconInlinedCodePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        Map<String, Object> props = new HashMap<>(pluginParams.getOpts().toMap());
        props.put("id", pluginParams.getFreeParam());
        return PluginResult.docElement("Icon", props);
    }
}
