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

package org.testingisdocumenting.znai.utils

import org.junit.Test

import static NameUtils.idFromTitle
import static NameUtils.dashToCamelCaseWithSpaces

class NameUtilsTest {
    @Test
    void "converts title to id stripping spaces and punctuation"() {
        assert idFromTitle(null) == null
        assert idFromTitle('') == ''
        assert idFromTitle('word') == 'word'
        assert idFromTitle('Hello DearWorld') == 'hello-dearworld'
        assert idFromTitle('Hello!. %#$ Dear/World?') == 'hello-dear-world'
        assert idFromTitle('Negative-Value Tests') == 'negative-value-tests'
    }

    @Test
    void "converts dashes to camel case with spaces"() {
        assert dashToCamelCaseWithSpaces(null) == null
        assert dashToCamelCaseWithSpaces('') == ''
        assert dashToCamelCaseWithSpaces('word') == 'Word'
        assert dashToCamelCaseWithSpaces('hello-dear-world') == 'Hello Dear World'
    }
}
