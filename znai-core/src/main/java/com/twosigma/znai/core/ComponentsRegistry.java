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

package com.twosigma.znai.core;

import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.commonmark.MarkdownParser;
import com.twosigma.znai.structure.DocStructure;
import com.twosigma.znai.extensions.include.IncludePlugin;

/**
 * simple components registry to avoid introduction of DI frameworks.
 * One place where we don't control dependencies passing is {@link IncludePlugin}
 *
 * @see IncludePlugin
 */
public interface ComponentsRegistry {
    /**
     * documentation wide default parser, can be markdown or any other parser that is used to build a documentation.
     * @return instance of a default parser
     */
    MarkupParser defaultParser();

    /**
     * markdown specific parser
     * @return markdown parser
     */
    MarkdownParser markdownParser();

    ResourcesResolver resourceResolver();
    DocStructure docStructure();

    GlobalAssetsRegistry globalAssetsRegistry();
}
