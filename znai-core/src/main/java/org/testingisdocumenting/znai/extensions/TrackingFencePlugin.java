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

package org.testingisdocumenting.znai.extensions;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.stream.Stream;

public class TrackingFencePlugin implements FencePlugin {
    private final FencePlugin delegate;
    private final PluginParamsTracker tracker;

    public TrackingFencePlugin(FencePlugin delegate, PluginParamsTracker tracker) {
        this.delegate = delegate;
        this.tracker = tracker;
    }

    @Override
    public String id() {
        return delegate.id();
    }

    @Override
    public FencePlugin create() {
        return delegate.create();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        tracker.trackParams(pluginParams);
        return delegate.process(componentsRegistry, markupPath, pluginParams, content);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return delegate.auxiliaryFiles(componentsRegistry);
    }

    @Override
    public SearchText textForSearch() {
        return delegate.textForSearch();
    }

    @Override
    public PluginParamsDefinition parameters() {
        return delegate.parameters();
    }
}
