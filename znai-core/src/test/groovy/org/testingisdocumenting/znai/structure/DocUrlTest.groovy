/*
 * Copyright 2023 znai maintainers
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

package org.testingisdocumenting.znai.structure

import org.junit.Test

class DocUrlTest {
    @Test
    void "parse with extension, reference back and anchor"() {
        def url = new DocUrl("../chapter/name.md#page-section")
        url.should == [anchorId: "page-section", tocItemFilePath: "../chapter/name.md", dirName: "", fileNameWithoutExtension: ""]
    }

    @Test
    void "parse with extension, reference current dir with dot and anchor"() {
        def url = new DocUrl("./chapter/name.md#page-section")
        url.should == [anchorId: "page-section", tocItemFilePath: "./chapter/name.md", dirName: "", fileNameWithoutExtension: ""]
    }

    @Test
    void "parse with mdx extension"() {
        def url = new DocUrl("./chapter/name.mdx#page-section")
        url.should == [anchorId: "page-section", tocItemFilePath: "./chapter/name.mdx", dirName: "", fileNameWithoutExtension: ""]
    }

    @Test
    void "parse without extension, reference back and anchor"() {
        def url = new DocUrl("../chapter/name#page-section")
        url.should == [anchorId: "page-section", dirName: "chapter", fileNameWithoutExtension: "name"]
    }
}
