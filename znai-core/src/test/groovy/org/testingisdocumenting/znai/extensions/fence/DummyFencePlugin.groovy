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

package org.testingisdocumenting.znai.extensions.fence

import org.testingisdocumenting.znai.core.ComponentsRegistry
import org.testingisdocumenting.znai.extensions.PluginParams
import org.testingisdocumenting.znai.extensions.PluginResult
import org.testingisdocumenting.znai.parser.docelement.DocElement

import java.nio.file.Path

class DummyFencePlugin implements FencePlugin {
    @Override
    String id() {
        return "dummy"
    }

    @Override
    FencePlugin create() {
        return new DummyFencePlugin()
    }

    @Override
    PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        def dummy = new DocElement("FenceDummy")
        if (pluginParams.freeParam) {
            dummy.addProp("freeParam", pluginParams.freeParam)
        }

        dummy.addProp("content", content)

        pluginParams.opts.forEach { k, v -> dummy.addProp(k, v)}

        return PluginResult.docElements([dummy].stream())
    }
}
