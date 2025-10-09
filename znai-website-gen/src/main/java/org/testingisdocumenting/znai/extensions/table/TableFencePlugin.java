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
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;

public class TableFencePlugin implements FencePlugin {
    private TableDocElementFromParams docElementFromParams;
    private MarkupTableDataFromContentAndParams tableDataFromContentAndParams;

    @Override
    public String id() {
        return "table";
    }

    @Override
    public FencePlugin create() {
        return new TableFencePlugin();
    }

    @Override
    public void preprocess(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        tableDataFromContentAndParams = new MarkupTableDataFromContentAndParams(componentsRegistry, pluginParams, content);
    }

    @Override
    public PluginParamsDefinition parameters() {
        return TablePluginParams.paramsFromColumnNames(tableDataFromContentAndParams);
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        MarkupParser parser = componentsRegistry.defaultParser();
        String fileName = pluginParams.getFreeParam();

        docElementFromParams = new TableDocElementFromParams(componentsRegistry, tableDataFromContentAndParams,
                markupPath, pluginParams, parser,
                resourcesResolver.fullPath(fileName));
        return docElementFromParams.create();
    }

    @Override
    public List<SearchText> textForSearch() {
        return List.of(SearchScore.STANDARD.text(docElementFromParams.getModifiedTable().allText()));
    }
}
