/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.extensions.latex;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.nio.file.Path;

public class LatexInlinedCodePlugin implements InlinedCodePlugin {
    public static final String ID = "latex";
    public static final String SRC_KEY = "src";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public InlinedCodePlugin create() {
        return new LatexInlinedCodePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        String latex = pluginParams.getOpts().getRequiredString(SRC_KEY);
        return PluginResult.docElement(new DocElement("InlinedLatex", "latex", latex));
    }
}
