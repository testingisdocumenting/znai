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

package org.testingisdocumenting.znai.parser.sphinx.python;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class PythonFunctionIncludePlugin implements IncludePlugin {
    private Map<String, Object> opts;

    @Override
    public String id() {
        return "python-function";
    }

    @Override
    public IncludePlugin create() {
        return new PythonFunctionIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        opts = pluginParams.getOpts().toMap();
        return PluginResult.docElement("LangFunction", opts);
    }

    @Override
    public List<SearchText> textForSearch() {
        if (opts == null) {
            return List.of();
        }

        StringBuilder searchText = new StringBuilder();
        if (opts.containsKey("name")) {
            searchText.append(opts.get("name")).append(" ");
        }

        if (opts.containsKey("description")) {
            searchText.append(opts.get("description")).append(" ");
        }

        return !searchText.isEmpty() ?
                List.of(SearchScore.HIGH.text(searchText.toString().trim())) :
                List.of();
    }
}
