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

package org.testingisdocumenting.znai.cpp.extensions

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.junit.Assert.assertEquals

class CppCommentsIncludePluginTest {
    @Test
    void "should extract comments with @znai at the beginning from specified entry"() {
        def actualMarkup = process("code-with-docs.cpp", "class_name::func2")
        def expectedMarkup = "func2 special comment because it was marked as documentation related\n" +
                "\n" +
                "func2 comment that is across\n" +
                "multiple lines"

        assertEquals(expectedMarkup, actualMarkup)
    }

    private static def process(String fileName, String entry) {
        def result = PluginsTestUtils.process(":include-cpp-comments: $fileName {entry: \"$entry\"}")
        return result[0].getProp("markup")
    }
}
