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

package com.twosigma.znai.extensions.inlinedcode

import com.twosigma.znai.core.ComponentsRegistry
import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.extensions.PluginResult
import com.twosigma.znai.parser.docelement.DocElement

import java.nio.file.Path

class DummyInlinedCodePlugin implements InlinedCodePlugin {
    @Override
    String id() {
        return "dummy"
    }

    @Override
    InlinedCodePlugin create() {
        return new DummyInlinedCodePlugin()
    }

    @Override
    PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        def dummy = new DocElement("InlinedCodeDummy", "ff", pluginParams.freeParam,
                "opts", pluginParams.opts.toMap())

        return PluginResult.docElements([dummy].stream())
    }
}
