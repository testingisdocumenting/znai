/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.extensions.table;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;

public class TableFencePlugin implements FencePlugin {
    private TableDocElementFromParams docElementFromParams;

    @Override
    public String id() {
        return "table";
    }

    @Override
    public FencePlugin create() {
        return new TableFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        MarkupParser parser = componentsRegistry.defaultParser();
        String fileName = pluginParams.getFreeParam();

        docElementFromParams = new TableDocElementFromParams(pluginParams, parser,
                componentsRegistry.resourceResolver().fullPath(fileName), content);
        return docElementFromParams.create();
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(docElementFromParams.getRearrangedTable().allText());
    }
}
