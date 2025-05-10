/*
 * Copyright 2021 znai maintainers
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

import java.nio.file.Paths

class FilePathUtilsTest {
    @Test
    void "should extract file name without extension from given path"() {
        assert FilePathUtils.fileNameWithoutExtension(Paths.get("file-name.md")) == "file-name"

        assert FilePathUtils.fileNameWithoutExtension(Paths.get("file-name.")) == "file-name"
        assert FilePathUtils.fileNameWithoutExtension(Paths.get("file-name")) == "file-name"
        assert FilePathUtils.fileNameWithoutExtension(Paths.get("")) == ""
    }

    @Test
    void "should replace extension"() {
        assert FilePathUtils.replaceExtension("/path/to/file-name.md", "json") == "/path/to/file-name.json"
    }

    @Test
    void "should extract extension"() {
        assert FilePathUtils.fileExtension("/path/to/file-name.md") == "md"
        assert FilePathUtils.fileExtension("/path/to/file-name") == ""
    }
}
