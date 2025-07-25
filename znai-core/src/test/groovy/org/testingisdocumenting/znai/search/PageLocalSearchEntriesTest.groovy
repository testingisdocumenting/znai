/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.search

import org.testingisdocumenting.znai.parser.PageSectionIdTitle
import org.testingisdocumenting.znai.structure.TocItem
import org.junit.Test

class PageLocalSearchEntriesTest {
    @Test
    void "should generate list representation of entries for local search indexer"() {
        def searchEntries = new PageLocalSearchEntries(
                new TocItem("dir-name", "file-name", "md"),
                [
                        new PageSearchEntry(new PageSectionIdTitle("section one", [:]), [SearchScore.STANDARD.text("hello world"), SearchScore.HIGH.text("snippet-one")]),
                        new PageSearchEntry(new PageSectionIdTitle("section two", [:]), [SearchScore.STANDARD.text("how is the weather")]),
                ])

        searchEntries.toListOfLists().should == [
                ["dir-name@@file-name@@section-one", "Dir Name", "File Name", "section one", "hello world", "snippet-one"],
                ["dir-name@@file-name@@section-two", "Dir Name", "File Name", "section two", "how is the weather", ""]]
    }

    @Test
    void "should handle empty chapter"() {
        def searchEntries = new PageLocalSearchEntries(
                new TocItem("", "overview.md", "md"),  // Standalone page
                [
                        new PageSearchEntry(new PageSectionIdTitle("overview", [:]), [SearchScore.STANDARD.text("project overview")]),
                ])

        searchEntries.toListOfLists().should == [
                ["@@overview@@overview", "", "Overview", "overview", "project overview", ""]]
    }
}
