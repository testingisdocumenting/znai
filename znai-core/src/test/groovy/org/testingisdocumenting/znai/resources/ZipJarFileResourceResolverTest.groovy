/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.resources

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.znai.console.ConsoleOutputs
import org.testingisdocumenting.znai.console.ansi.AnsiConsoleOutput
import org.testingisdocumenting.znai.console.ansi.Color
import org.testingisdocumenting.znai.core.Log

import java.nio.file.Paths

class ZipJarFileResourceResolverTest implements Log {
    static def consoleOutput = new AnsiConsoleOutput()

    @BeforeClass
    static void init() {
        ConsoleOutputs.add(consoleOutput)
    }

    @AfterClass
    static void cleanup() {
        ConsoleOutputs.remove(consoleOutput)
    }

    @Test
    void "text content of archived files"() {
        def zipResourceResolver = new ZipJarFileResourceResolver(this, Paths.get(""))

        zipResourceResolver.initialize([
                "src/test/resources/files.zip",
                "src/test/resources/files.jar",
        ].stream())

        zipResourceResolver.textContent("inside-zip-a.txt").should == "inside\nzip\nA"
        zipResourceResolver.textContent("dir/inside-zip-b.txt").should == "inside\nzip\nB"
        zipResourceResolver.textContent("inside-jar-a.txt").should == "inside\njar\nA"
    }

    @Override
    void phase(String message) {
        ConsoleOutputs.out(Color.BLUE, message)
    }

    @Override
    void info(Object... styleOrValue) {
        ConsoleOutputs.out(styleOrValue)
    }

    @Override
    void warn(String message) {
        throw new RuntimeException(message)
    }
}
