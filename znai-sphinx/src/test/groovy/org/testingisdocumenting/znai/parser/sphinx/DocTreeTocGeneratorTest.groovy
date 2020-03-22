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

package org.testingisdocumenting.znai.parser.sphinx

import org.testingisdocumenting.znai.utils.ResourceUtils
import org.junit.Test

class DocTreeTocGeneratorTest {
    @Test
    void "generates table of contents from index xml file"() {
        def toc = new DocTreeTocGenerator().generate(ResourceUtils.textContent("test-index.xml"))

        toc.contains("chapter-one", "page-three", "").should == true
        toc.contains("chapter-two", "page-four", "").should == true
        toc.contains("chapter-two", "page-five", "").should == true

        toc.contains("wrong-chapter", "garbage", "").should == false
    }
}
