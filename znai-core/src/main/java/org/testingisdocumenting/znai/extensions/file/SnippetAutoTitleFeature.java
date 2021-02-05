/*
 * Copyright 2021 znai maintainers
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
import org.testingisdocumenting.znai.extensions.features.PluginFeature;

import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;

public class SnippetAutoTitleFeature implements PluginFeature {
    private static final String TITLE_KEY = "title";
    private static final String AUTO_TITLE_KEY = "autoTitle";

    private final SnippetContentProvider snippetContentProvider;

    public SnippetAutoTitleFeature(SnippetContentProvider snippetContentProvider) {
        this.snippetContentProvider = snippetContentProvider;
    }

    @Override
    public void updateProps(Map<String, Object> props) {
        if (props.containsKey(TITLE_KEY) && props.containsKey(AUTO_TITLE_KEY)) {
            throw new IllegalArgumentException("Can't have both <" + TITLE_KEY + "> and <" +
                    AUTO_TITLE_KEY + "> specified");
        }

        Object autoTitle = props.get(AUTO_TITLE_KEY);
        if (autoTitle == null) {
            return;
        }

        if (TRUE.equals(autoTitle)) {
            props.put(TITLE_KEY, snippetContentProvider.snippetId());
        }
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles() {
        return Stream.empty();
    }
}
