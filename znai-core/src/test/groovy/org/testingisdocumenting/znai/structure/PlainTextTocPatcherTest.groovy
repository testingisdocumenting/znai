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

package org.testingisdocumenting.znai.structure

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class PlainTextTocPatcherTest {
    @Test
    void "should remove, replace and add items based on patch instructions"() {
        def toc = new TableOfContents()
        toc.addTocItem(new TocNameAndOpts('ch1'), 'p1')
        toc.addTocItem(new TocNameAndOpts('ch1'), 'p2')
        toc.addTocItem(new TocNameAndOpts('ch2'), 'p3')
        toc.addTocItem(new TocNameAndOpts('ch2'), 'p4')

        def patcher = new PlainTextTocPatcher(toc)
        patcher.patch("remove ch1/p2\n" +
                "replace ch1/p1 ch1/p1_\n" +
                "add ch1/p5")

        def asMap = toc.tocItems*.toMap()
        asMap.dirName.should == ['ch1', 'ch2', 'ch2', 'ch1']
        asMap.fileName.should == ['p1_', 'p3', 'p4', 'p5']
    }

    @Test
    void "should clarify path format when format mismatches"() {
        def toc = new TableOfContents()
        def patcher = new PlainTextTocPatcher(toc)

        code {
            patcher.patch('abc')
        } should throwException('wrong patch file format. \n' +
                'expect: command arg1 [arg2]\n' +
                'received: abc')

        code {
            patcher.patch('abc file')
        } should throwException('wrong command argument.\n' +
                'expect: dir-name/file-name\n' +
                'received: file')

        code {
            patcher.patch('abc dir/file')
        } should throwException('unrecognized command: abc')
    }
}
