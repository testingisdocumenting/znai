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

package org.testingisdocumenting.znai.structure

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class TocItemTest {
    @Test
    void "should not allow special symbols in file name"() {
        def ignore = new TocItem('dir-name', 'file-name', 'md')

        shouldThrow('dir-name', 'fileName?')
        shouldThrow('dir-name?', 'fileName')
        shouldThrow('dir!-name#', 'fileName!')
    }

    @Test
    void "should use default file name extension"() {
        def tocItem = new TocItem('dir-name', 'file-name', 'mdx')
        tocItem.dirName.should == "dir-name"
        tocItem.fileNameWithoutExtension.should == "file-name"
        tocItem.fileExtension.should == "mdx"
    }

    @Test
    void "should extract optional file name extension"() {
        def tocItem = new TocItem('dir-name', 'file-name.mdx', 'md')
        tocItem.dirName.should == "dir-name"
        tocItem.fileNameWithoutExtension.should == "file-name"
        tocItem.fileExtension.should == "mdx"
    }

    @Test
    void "should allow title override that is not overridable from outside"() {
        def tocItem = new TocItem(new TocNameAndOpts('dir-name {title: "my chapter"}'),
                new TocNameAndOpts('file-name.mdx {title: "my page"}'), 'md')

        tocItem.chapterTitle.should == "my chapter"
        tocItem.pageTitle.should == "my page"

        tocItem.setPageTitleIfNoTocOverridePresent("override inside markdown")
        tocItem.pageTitle.should == "my page"
    }

    private static void shouldThrow(String dirName, String fileName) {
        code {
            new TocItem(dirName, fileName, 'md')
        } should throwException(IllegalArgumentException)
    }
}
