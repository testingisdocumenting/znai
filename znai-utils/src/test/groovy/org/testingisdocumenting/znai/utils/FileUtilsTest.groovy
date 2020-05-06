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

import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files

class FileUtilsTest {
    @Test
    void "should read text content from a file"() {
        def testFile = new File("dummy.txt")
        testFile.deleteOnExit()

        Files.write(testFile.toPath(), ["content of a file \u275e"])
        assert FileUtils.fileTextContent(testFile.toPath()) == "content of a file ❞"
    }

    @Test
    void "create dirs with non-existent dir"() {
        def path = Files.createTempDirectory("webtau_test")
        path.deleteDir()

        FileUtils.symlinkAwareCreateDirs(path)
    }

    @Test
    void "create dirs with existing empty dir"() {
        def path = Files.createTempDirectory("webtau_test")

        FileUtils.symlinkAwareCreateDirs(path)
    }

    @Test
    void "create dirs with existing non-empty dir"() {
        def path = Files.createTempDirectory("webtau_test")
        Files.createTempFile(path, "test", "")

        FileUtils.symlinkAwareCreateDirs(path)
    }

    @Test
    void "create dirs with non-dir path"() {
        def file = Files.createTempFile("webtau_test_file", "")

        try {
            FileUtils.symlinkAwareCreateDirs(file)
        } catch (RuntimeException e) {
            assert e.cause.class == FileAlreadyExistsException.class
        }
    }

    @Test
    void "create dirs with symlink pointing to directory"() {
        def path = Files.createTempDirectory("webtau_test")
        def symlink = path.parent.resolve("webtau_link_" + System.currentTimeMillis())
        Files.createSymbolicLink(symlink, path)

        FileUtils.symlinkAwareCreateDirs(symlink)
    }

    @Test
    void "create dirs with symlink pointing to file"() {
        def file = Files.createTempFile("webtau_test_file", "")
        def symlink = file.parent.resolve("webtau_link_" + System.currentTimeMillis())
        Files.createSymbolicLink(symlink, file)

        try {
            FileUtils.symlinkAwareCreateDirs(symlink)
        } catch (RuntimeException e) {
            assert e.cause.class == FileAlreadyExistsException.class
        }    }
}
