/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.extensions.include;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.Plugin;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;

public interface IncludePlugin extends Plugin {
    IncludePlugin create();

    /**
     * called before params validation and before process
     * can be used to pre-calculate some values for params validation
     * and then reuse the calculated for actual process
     * @param componentsRegistry registry of components
     * @param parserHandler handler to build doc elements explicitly
     * @param markupPath path of the parsed file where this plugin is used
     * @param pluginParams plugin parameters
     */
    default void preprocess(ComponentsRegistry componentsRegistry,
                            ParserHandler parserHandler,
                            Path markupPath,
                            PluginParams pluginParams) {
    }

    PluginResult process(ComponentsRegistry componentsRegistry,
                         ParserHandler parserHandler,
                         Path markupPath,
                         PluginParams pluginParams);
}
