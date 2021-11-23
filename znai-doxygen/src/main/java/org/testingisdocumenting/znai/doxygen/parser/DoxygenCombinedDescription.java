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

package org.testingisdocumenting.znai.doxygen.parser;

import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.util.stream.Stream;

public class DoxygenCombinedDescription {
    private final DoxygenBriefDescription brief;
    private final DoxygenDescription full;

    public DoxygenCombinedDescription(DoxygenBriefDescription brief, DoxygenDescription full) {
        this.brief = brief;
        this.full = full;
    }

    public DoxygenBriefDescription getBrief() {
        return brief;
    }

    public DoxygenDescription getFull() {
        return full;
    }

    public Stream<DocElement> docElementStream() {
        return Stream.concat(brief.getDocElements().stream(), full.getDocElements().stream());
    }

    public String textForSearch() {
        return brief.getSearchText() + " " + full.getSearchTextWithoutParameters();
    }
}
