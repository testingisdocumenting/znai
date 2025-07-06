/*
 * Copyright 2020 znai maintainers
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

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.search.PageSearchEntry;
import org.testingisdocumenting.znai.structure.PageMeta;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public record MarkupParserResult(DocElement docElement,
                                 List<String> globalAnchorIds,
                                 List<PageSearchEntry> searchEntries,
                                 List<AuxiliaryFile> auxiliaryFiles,
                                 PageMeta pageMeta,
                                 String markdown) {
    /**
     * Top level page element. use get content to get access to the children
     *
     * @return top level page element
     */
    @Override
    public DocElement docElement() {
        return docElement;
    }

    public List<Map<String, Object>> contentToListOfMaps() {
        return docElement.contentToListOfMaps();
    }

    public String getAllText() {
        return searchEntries.stream().map(PageSearchEntry::extractText)
                .collect(joining(" "));
    }
}
