/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.enterprise.monitoring

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.znai.console.ConsoleOutput
import org.testingisdocumenting.znai.console.ConsoleOutputs
import org.testingisdocumenting.znai.console.ansi.IgnoreAnsiString

import java.nio.file.Paths

class FilesFinderTest implements ConsoleOutput {
    def capturedErrors = []

    @Before
    void init() {
        ConsoleOutputs.add(this)
    }

    @After
    void cleanup() {
        ConsoleOutputs.remove(this)
    }

    @Test
    void "should find files by pattern"() {
        def found = new FilesFinder(
                [new BuildRootAndWildCardPatterns(
                        Paths.get(""),
                        ["src/test/groovy/**/*FinderTest.groovy"])]).find()

        found.size().should == 1
        found[0].fileName.toString().should == 'FilesFinderTest.groovy'
        found[0].parent.fileName.toString().should == 'monitoring'
    }

    @Test
    void "should catch errors and return empty list when no dir is found"() {
        def found = new FilesFinder(
                [new BuildRootAndWildCardPatterns(
                        Paths.get("/__wrong-dir"),
                        ["src/**/*FinderTest.groovy"])]).find()

        found.size().should == 0
        capturedErrors.should == ["scan error: basedir /__wrong-dir does not exist."]
    }

    @Override
    void out(Object... styleOrValues) {
    }

    @Override
    void err(Object... styleOrValues) {
        capturedErrors << new IgnoreAnsiString(Arrays.stream(styleOrValues)).toString()
    }
}
