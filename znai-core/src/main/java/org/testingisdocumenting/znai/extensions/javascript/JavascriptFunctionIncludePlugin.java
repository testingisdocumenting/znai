/*
 * Copyright 2026 znai maintainers
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

package org.testingisdocumenting.znai.extensions.javascript;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.utils.CollectionUtils;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class JavascriptFunctionIncludePlugin implements IncludePlugin {
    private String functionName;
    private Map<String, Object> args;

    @Override
    public String id() {
        return "javascript-function";
    }

    @Override
    public IncludePlugin create() {
        return new JavascriptFunctionIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        functionName = pluginParams.getFreeParam();
        if (functionName == null || functionName.isEmpty()) {
            throw new IllegalArgumentException("javascript function name must be provided as a free param," +
                    " e.g. :include-javascript-function: myFunction");
        }

        args = pluginParams.getOpts().toMap();

        return PluginResult.docElement("JavascriptFunction", CollectionUtils.createMap(
                "functionName", functionName,
                "args", args));
    }

    @Override
    public List<SearchText> textForSearch() {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(functionName);
        collectSearchableValues(args, joiner);

        return List.of(SearchScore.STANDARD.text(joiner.toString()));
    }

    private static void collectSearchableValues(Object value, StringJoiner joiner) {
        if (value == null) {
            return;
        }

        if (value instanceof Map) {
            ((Map<?, ?>) value).values().forEach(v -> collectSearchableValues(v, joiner));
        } else if (value instanceof Collection) {
            ((Collection<?>) value).forEach(v -> collectSearchableValues(v, joiner));
        } else {
            joiner.add(value.toString());
        }
    }
}
