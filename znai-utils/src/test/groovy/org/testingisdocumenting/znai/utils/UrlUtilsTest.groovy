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

package org.testingisdocumenting.znai.utils

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class UrlUtilsTest {

    @Test
    void "url concatenation validates passed url on the left to be non null"() {
        code {
            UrlUtils.concat(null, '/relative')
        } should throwException(IllegalArgumentException, 'passed url on the left is NULL')
    }

    @Test
    void "url concatenation validates passed url on the right to be non null"() {
        code {
            UrlUtils.concat('/relative', null)

        } should throwException(IllegalArgumentException, 'passed url on the right is NULL')
    }

    @Test
    void "url concatenation handles slashes to avoid double slash or slash absense"() {
        def expected = 'https://base/relative'

        UrlUtils.concat('https://base/', '/relative').should == expected
        UrlUtils.concat('https://base', '/relative').should == expected
        UrlUtils.concat('https://base/', 'relative').should == expected
        UrlUtils.concat('https://base', 'relative').should == expected
    }

    @Test
    void "is external"() {
        UrlUtils.isExternal("http://hello").should == true
        UrlUtils.isExternal("https://hello").should == true
        UrlUtils.isExternal("mailto://hello").should == true
        UrlUtils.isExternal("file://hello.txt").should == true
        UrlUtils.isExternal("httphello").should == false
    }
}
