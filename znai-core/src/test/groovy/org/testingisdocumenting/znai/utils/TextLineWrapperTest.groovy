/*
 * Copyright 2025 znai maintainers
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

import static TextLineWrapper.wrapLine

class TextLineWrapperTest {
    @Test
    void "handles null and empty input"() {
        assert wrapLine(null, 80) == null
        assert wrapLine("", 80) == ""
    }

    @Test
    void "does not wrap short lines"() {
        assert wrapLine("short line", 80) == "short line"
    }

    @Test
    void "wraps long line at word boundary"() {
        def result = wrapLine("This is a very long line that should be wrapped at word boundaries to stay within the maximum width limit", 50)

        assert result == """This is a very long line that should be wrapped at
word boundaries to stay within the maximum width
limit"""
    }

    @Test
    void "preserves multiple spaces"() {
        assert wrapLine("hello  world", 80) == "hello  world"

        def result = wrapLine("hello  world  with  double  spaces  that  needs  wrapping  here", 30)

        assert result == """hello  world  with  double
spaces  that  needs  wrapping
here"""
    }

    @Test
    void "punctuation stays with previous word"() {
        def result = wrapLine("This is a sentence. Another sentence follows here.", 30)

        assert result == """This is a sentence. Another
sentence follows here."""
    }
}
