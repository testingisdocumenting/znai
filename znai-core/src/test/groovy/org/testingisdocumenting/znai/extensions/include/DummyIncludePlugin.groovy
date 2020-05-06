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

package org.testingisdocumenting.znai.extensions.include

import org.testingisdocumenting.znai.core.ComponentsRegistry
import org.testingisdocumenting.znai.extensions.PluginParams
import org.testingisdocumenting.znai.extensions.PluginResult
import org.testingisdocumenting.znai.parser.ParserHandler
import org.testingisdocumenting.znai.parser.docelement.DocElement

import java.nio.file.Path

class DummyIncludePlugin implements IncludePlugin {
    @Override
    String id() {
        return "dummy"
    }

    @Override
    IncludePlugin create() {
        return new DummyIncludePlugin()
    }

    @Override
    PluginResult process(ComponentsRegistry componentsRegistry,
                         ParserHandler parserHandler,
                         Path markupPath,
                         PluginParams includeParams) {
        def dummy = new DocElement("IncludeDummy")
        dummy.addProp("ff", includeParams.getFreeParam())
        dummy.addProp("opts", includeParams.getOpts().toMap())

        return PluginResult.docElements([dummy].stream())
    }
}
