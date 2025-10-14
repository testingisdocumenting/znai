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

package org.testingisdocumenting.znai.charts;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;

public abstract class ChartFenceBasePlugin implements FencePlugin {
    private String csvContent;

    abstract protected String type();

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                Path markupPath,
                                PluginParams pluginParams,
                                String csvContent) {
        this.csvContent = csvContent;
        return ChartPluginResult.create(pluginParams, type(), csvContent);
    }

    @Override
    public List<SearchText> textForSearch() {
        return csvContent != null ? List.of(SearchScore.STANDARD.text(csvContent)) : List.of();
    }
}
