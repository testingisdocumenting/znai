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

package org.testingisdocumenting.znai.extensions.features;

import org.testingisdocumenting.znai.core.AuxiliaryFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PluginFeatureList implements PluginFeature {
    private final List<PluginFeature> list;

    public PluginFeatureList(PluginFeature... list) {
        this.list = new ArrayList<>(Arrays.asList(list));
    }

    public void add(PluginFeature feature) {
        list.add(feature);
    }

    @Override
    public void updateProps(Map<String, Object> props) {
        list.forEach(pluginFeature -> pluginFeature.updateProps(props));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles() {
        Stream<AuxiliaryFile> result = Stream.empty();
        for (PluginFeature pluginFeature : list) {
            result = Stream.concat(result, pluginFeature.auxiliaryFiles());
        }

        return result;
    }

    public Stream<AuxiliaryFile> combineAuxiliaryFilesWith(Stream<AuxiliaryFile> auxiliaryFile) {
        return Stream.concat(auxiliaryFile, auxiliaryFiles());
    }
}
