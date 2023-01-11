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

package org.testingisdocumenting.znai.extensions.file;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.features.PluginFeature;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Provides reveal stop line validation and conversion from text to indexes
 */
public class SnippetRevealLineStopFeature implements PluginFeature {
    public static final PluginParamsDefinition paramsDefinition = createParamsDefinition();

    private static final String REVEAL_LINE_STOP_KEY = "revealLineStop";
    private final SnippetContentProvider contentProvider;
    private final List<Object> revealLineStop;

    public SnippetRevealLineStopFeature(PluginParams pluginParams,
                                        SnippetContentProvider contentProvider) {
        this.contentProvider = contentProvider;
        this.revealLineStop = pluginParams.getOpts().getList(REVEAL_LINE_STOP_KEY);
    }

    @Override
    public void updateProps(Map<String, Object> props) {
        if (!lineStopsProvided()) {
            return;
        }

        SnippetContainerEntriesConverter snippetValidator = new SnippetContainerEntriesConverter(
                contentProvider.snippetId(),
                SnippetCleaner.removeNonAnsiCharacters(contentProvider.snippetContent()),
                REVEAL_LINE_STOP_KEY);
        props.put(REVEAL_LINE_STOP_KEY, snippetValidator.convertAndValidate(revealLineStop));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles() {
        return Stream.empty();
    }

    private boolean lineStopsProvided() {
        return !revealLineStop.isEmpty();
    }

    private static PluginParamsDefinition createParamsDefinition() {
        return new PluginParamsDefinition()
                .add(REVEAL_LINE_STOP_KEY, PluginParamType.LIST_OR_SINGLE_STRING_OR_NUMBER,
                        "reveal line stop indexes for presentation", "[3, \"partial-match-text\"]");
    }
}
