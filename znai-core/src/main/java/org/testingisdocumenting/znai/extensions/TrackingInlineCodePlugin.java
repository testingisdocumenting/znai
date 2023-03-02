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
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.stream.Stream;

public class TrackingInlineCodePlugin implements InlinedCodePlugin {
    private final InlinedCodePlugin delegate;
    private final PluginParamsTracker paramsTracker;

    public TrackingInlineCodePlugin(InlinedCodePlugin delegate, PluginParamsTracker paramsTracker) {
        this.delegate = delegate;
        this.paramsTracker = paramsTracker;
    }

    @Override
    public PluginParamsDefinition parameters() {
        return delegate.parameters();
    }

    @Override
    public String id() {
        return delegate.id();
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
    public InlinedCodePlugin create() {
        return delegate.create();
    }

    @Override
    public void preprocess(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        delegate.preprocess(componentsRegistry, markupPath, pluginParams);
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        paramsTracker.trackParams(pluginParams);
        return delegate.process(componentsRegistry, markupPath, pluginParams);
    }
}
