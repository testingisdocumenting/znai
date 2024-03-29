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

import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.text.TextContentExtractor;

public class ManipulatedSnippetContentProvider implements SnippetContentProvider {
    public static final PluginParamsDefinition paramsDefinition = TextContentExtractor.createParamsDefinition();

    private final String snippetId;
    private final String extractedContent;

    public ManipulatedSnippetContentProvider(String snippetId, String originalContent,
                                             PluginParams pluginParams) {
        this.snippetId = snippetId;
        this.extractedContent = TextContentExtractor.extractText(snippetId, originalContent, pluginParams.getOpts());
    }

    @Override
    public String snippetContent() {
        return extractedContent;
    }

    @Override
    public String snippetId() {
        return snippetId;
    }
}
