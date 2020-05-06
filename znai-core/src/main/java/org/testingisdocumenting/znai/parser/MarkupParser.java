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

package org.testingisdocumenting.znai.parser;

import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.structure.PageMeta;

import java.nio.file.Path;

/**
 * Parser for markup languages. Markup in {@link DocElement} out. Markup here stands for generic human readable language.
 */
public interface MarkupParser {
    /**
     * parses markup
     *
     * @param path   path of the content, will be used to resolve resources location (as one of the options)
     * @param markup markup to parse
     * @return markup result
     * @see MarkupPathsResolution
     */
    MarkupParserResult parse(Path path, String markup);

    /**
     * parses only page meta
     * @param markup markup to parse
     * @return page meta
     */
    PageMeta parsePageMetaOnly(String markup);
}