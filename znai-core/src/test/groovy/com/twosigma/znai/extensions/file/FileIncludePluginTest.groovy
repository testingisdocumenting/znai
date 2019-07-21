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

package com.twosigma.znai.extensions.file

import com.twosigma.znai.extensions.include.PluginsTestUtils
import org.junit.Assert
import org.junit.Test

class FileIncludePluginTest {
    @Test
    void "should extract file snippet based on start line and number of lines"() {
        def text = process("file.txt", "{startLine: 'multiple lines', numberOfLines: 2}")

        Assert.assertEquals("a multiple lines\n" +
                "line number", text)
    }

    @Test
    void "should extract file snippet based on start and stop end lines"() {
        def text = process("file.txt", "{startLine: 'multiple lines', endLine: 'stop'}")

        Assert.assertEquals("a multiple lines\n" +
                "line number\n" +
                "--- stop", text)
    }

    @Test
    void "should extract file snippet based on start and stop end lines excluding them"() {
        def text = process("file.txt", "{startLine: 'number', endLine: 'stop', exclude: true}")

        Assert.assertEquals("", text)
    }

    @Test
    void "should extract file snippet based on start line only"() {
        def text = process("file.txt", "{startLine: 'multiple lines'}")

        Assert.assertEquals("a multiple lines\n" +
                "line number\n" +
                "--- stop\n" +
                "and five\n" +
                "and then six", text)
    }

    @Test
    void "should extract file snippet based on end line only"() {
        def text = process("file.txt", "{endLine: 'stop'}")

        Assert.assertEquals("this is a\n" +
                "test file in\n" +
                "a multiple lines\n" +
                "line number\n" +
                "--- stop", text)
    }

    @Test
    void "should automatically strip extra indentation"() {
        def text = process("script.groovy", "{startLine: 'class', endLine: '}', exclude: true}")

        Assert.assertEquals(
                "def a\n" +
                "def b", text)
    }

    @Test
    void "should only include lines matching regexp"() {
        def singleAssert = process("script.groovy", "{includeRegexp: 'import.*ClassName'}")
        Assert.assertEquals("import a.b.c.ClassName", singleAssert)

        def allAsserts = process("script.groovy", "{includeRegexp: 'import'}")
        Assert.assertEquals(
                "import e.d.g.AnotherName\n" +
                "import a.b.c.ClassName", allAsserts)
    }

    private static String process(String fileName, String value) {
        return PluginsTestUtils.processAndGetSimplifiedCodeBlock(":include-file: $fileName $value")
    }
}
