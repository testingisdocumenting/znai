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
        var ignore = new TocItem('dir-name', 'file-name', 'md')

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
    void "should create title from file name ignoring extension"() {
        def tocItem = new TocItem('dir-name', 'file-name.mdx', 'md')
        tocItem.dirName.should == "dir-name"
        tocItem.fileNameWithoutExtension.should == "file-name"
        tocItem.pageTitle.should == "File Name"
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

    static void validatePath(tocItem, path) {
        tocItem.getFilePath().should == path
    }

    @Test
    void "should allow to specify markup file path"() {
        validatePath(new TocItem(new TocNameAndOpts('dir-name'),
                new TocNameAndOpts('my-id {path: "nested-dir/file.mdx"}'), 'md'),
        "dir-name/nested-dir/file.mdx")

        validatePath(new TocItem(new TocNameAndOpts('dir-name'),
                new TocNameAndOpts('my-id {path: "/nested-dir/file.mdx"}'), 'md'),
                "/nested-dir/file.mdx")

        validatePath(new TocItem(new TocNameAndOpts('dir-name'),
                new TocNameAndOpts('my-id {path: "../file.mdx"}'), 'md'),
                "dir-name/../file.mdx")
    }

    @Test
    void "should identify standalone pages correctly"() {
        def standalonePage = new TocItem('', 'overview.md', 'md')
        standalonePage.isIndex().should == false

        def chapterPage = new TocItem('chapter1', 'page.md', 'md') 
        chapterPage.isIndex().should == false
    }

    private static void shouldThrow(String dirName, String fileName) {
        code {
            new TocItem(dirName, fileName, 'md')
        } should throwException(IllegalArgumentException)
    }
}
