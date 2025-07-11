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

package org.testingisdocumenting.znai.extensions;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface Plugin {
    String id();

    Plugin create();

    /**
     * called before params validation and before process
     * can be used to pre-calculate some values for params validation
     * and then reuse the calculated for actual process
     * @param componentsRegistry registry of components
     * @param markupPath path of the parsed file where this plugin is used
     * @param pluginParams plugin parameters
     */
    default void preprocess(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
    }

    default Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.empty();
    }

    default SearchText textForSearch() {
        return null;
    }

    default String markdownRepresentation() {
        return "";
    }

    default PluginParamsDefinition parameters() { return PluginParamsDefinition.undefined(); }
}
