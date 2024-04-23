/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.extensions.markup;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;

import java.nio.file.Path;

public class MarkdownInlinedCodePlugin extends MarkdownBasePlugin implements InlinedCodePlugin {
    @Override
    public InlinedCodePlugin create() {
        return new MarkdownInlinedCodePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        DocElement page = parserResult.docElement();
        if (page.getContent().isEmpty()) {
            return PluginResult.empty();
        }

        if (page.getContent().size() == 1) {
            DocElement firstElement = page.getContent().get(0);
            if (firstElement.getType().equals(DocElementType.PARAGRAPH)) {
                return PluginResult.docElements(firstElement.getContent().stream());
            }
        }

        return PluginResult.docElements(page.getContent().stream());
    }
}
