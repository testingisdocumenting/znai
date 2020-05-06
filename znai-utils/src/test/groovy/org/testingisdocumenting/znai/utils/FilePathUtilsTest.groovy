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
}
