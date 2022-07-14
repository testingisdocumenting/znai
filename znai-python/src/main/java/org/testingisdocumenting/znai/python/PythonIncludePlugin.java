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

package org.testingisdocumenting.znai.python;

import org.testingisdocumenting.znai.codesnippets.CodeSnippetsProps;
import org.testingisdocumenting.znai.extensions.file.ManipulatedSnippetContentProvider;
import org.testingisdocumenting.znai.extensions.file.SnippetAutoTitleFeature;
import org.testingisdocumenting.znai.extensions.file.SnippetHighlightFeature;
import org.testingisdocumenting.znai.extensions.file.SnippetRevealLineStopFeature;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public class PythonIncludePlugin extends PythonIncludePluginBase {
    private boolean isBodyOnly;

    @Override
    public String id() {
        return "python";
    }

    @Override
    public IncludePlugin create() {
        return new PythonIncludePlugin();
    }

    @Override
    public PythonIncludeResult process(PythonParsedFile parsed, ParserHandler parserHandler, Path markupPath) {
        isBodyOnly = pluginParams.getOpts().get("bodyOnly", false);

        PythonParsedEntry codeEntry = findEntryByName(parsed, getEntryName());

        ManipulatedSnippetContentProvider contentProvider = new ManipulatedSnippetContentProvider(snippetIdToUse(),
                extractContent(codeEntry),
                pluginParams);

        features.add(new SnippetAutoTitleFeature(contentProvider.snippetId()));
        features.add(new SnippetHighlightFeature(componentsRegistry, pluginParams, contentProvider));
        features.add(new SnippetRevealLineStopFeature(pluginParams, contentProvider));

        Map<String, Object> props = CodeSnippetsProps.create("python", contentProvider.snippetContent());
        props.putAll(pluginParams.getOpts().toMap());
        features.updateProps(props);

        DocElement docElement = new DocElement(DocElementType.SNIPPET);
        docElement.addProps(props);

        return new PythonIncludeResult(Collections.singletonList(docElement), contentProvider.snippetContent());
    }

    private String extractContent(PythonParsedEntry codeEntry) {
        if (isBodyOnly) {
            return codeEntry.getBodyOnly();
        }

        return codeEntry.getContent();
    }
}
