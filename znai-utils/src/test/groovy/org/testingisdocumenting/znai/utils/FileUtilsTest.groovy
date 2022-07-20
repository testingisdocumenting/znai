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
import java.nio.file.Paths

class FileUtilsTest {
    @Test
    void "delete dir with sub dirs and files"() {
        def root = Files.createTempDirectory("znai_test")
        Files.createDirectory(root.resolve("d1"))
        Files.createDirectory(root.resolve("d2"))

        FileUtils.writeTextContent(root.resolve("d1").resolve("file1.txt"), "hello")
        FileUtils.writeTextContent(root.resolve("d2").resolve("file2.txt"), "hello")

        FileUtils.deleteFileOrDirQuietly(root)

        assert !Files.exists(root)
    }

    @Test
    void "delete file"() {
        def root = Files.createTempDirectory("znai_test")
        def file1 = root.resolve("file1.txt")

        FileUtils.writeTextContent(file1, "hello")

        FileUtils.deleteFileOrDirQuietly(file1)
        assert !Files.exists(file1)

        FileUtils.deleteFileOrDirQuietly(root)
        assert !Files.exists(root)
    }

    @Test
    void "delete non existing file"() {
        FileUtils.deleteFileOrDirQuietly(Paths.get("non-existing-file"))
    }

    @Test
    void "should read text content from a file"() {
        def testFile = new File("dummy.txt")
        testFile.deleteOnExit()

        Files.write(testFile.toPath(), ["content of a file \u275e"])
        assert FileUtils.fileTextContent(testFile.toPath()) == "content of a file ❞"
    }

    @Test
    void "create dirs with non-existent dir"() {
        def path = Files.createTempDirectory("znai_test")
        path.deleteDir()

        FileUtils.symlinkAwareCreateDirs(path)
    }

    @Test
    void "create dirs with existing empty dir"() {
        def path = Files.createTempDirectory("znai_test")

        FileUtils.symlinkAwareCreateDirs(path)
    }

    @Test
    void "create dirs with existing non-empty dir"() {
        def path = Files.createTempDirectory("znai_test")
        Files.createTempFile(path, "test", "")

        FileUtils.symlinkAwareCreateDirs(path)
    }

    @Test
    void "create dirs with non-dir path"() {
        def file = Files.createTempFile("znai_test_file", "")

        try {
            FileUtils.symlinkAwareCreateDirs(file)
        } catch (RuntimeException e) {
            assert e.cause.class == FileAlreadyExistsException.class
        }
    }

    @Test
    void "create dirs with symlink pointing to directory"() {
        def path = Files.createTempDirectory("znai_test")
        def symlink = path.parent.resolve("znai_link_" + System.currentTimeMillis())
        Files.createSymbolicLink(symlink, path)

        FileUtils.symlinkAwareCreateDirs(symlink)
    }

    @Test
    void "create dirs with symlink pointing to file"() {
        def file = Files.createTempFile("znai_test_file", "")
        def symlink = file.parent.resolve("znai_link_" + System.currentTimeMillis())
        Files.createSymbolicLink(symlink, file)

        try {
            FileUtils.symlinkAwareCreateDirs(symlink)
        } catch (RuntimeException e) {
            assert e.cause.class == FileAlreadyExistsException.class
        }    }
}
