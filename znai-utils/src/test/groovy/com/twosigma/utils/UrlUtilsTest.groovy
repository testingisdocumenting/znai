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

package com.twosigma.utils

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class UrlUtilsTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none()

    @Test
    void "url concatenation validates passed url on the left to be non null"() {
        exception.expect(IllegalArgumentException)
        exception.expectMessage('passed url on the left is NULL')

        UrlUtils.concat(null, '/relative')
    }

    @Test
    void "url concatenation validates passed url on the right to be non null"() {
        exception.expect(IllegalArgumentException)
        exception.expectMessage('passed url on the right is NULL')

        UrlUtils.concat('/relative', null)
    }

    @Test
    void "url concatenation handles slashes to avoid double slash or slash absense"() {
        def expected = 'https://base/relative'

        assert UrlUtils.concat('https://base/', '/relative') == expected
        assert UrlUtils.concat('https://base', '/relative') == expected
        assert UrlUtils.concat('https://base/', 'relative') == expected
        assert UrlUtils.concat('https://base', 'relative') == expected
    }
}
