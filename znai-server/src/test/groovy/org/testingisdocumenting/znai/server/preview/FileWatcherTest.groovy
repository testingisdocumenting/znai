/*
 * Copyright 2026 znai maintainers
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

package org.testingisdocumenting.znai.server.preview

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.znai.website.WebSite

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import java.util.concurrent.ConcurrentLinkedQueue

import static org.testingisdocumenting.webtau.WebTauCore.*

class FileWatcherTest {
    private static final long TIMEOUT_MILLIS = 30_000

    Path root
    RecordingFileChangeHandler changeHandler
    FileWatcher fileWatcher
    Thread watcherThread

    @Before
    void setUp() {
        root = Files.createTempDirectory("znai-file-watcher-test")
        changeHandler = new RecordingFileChangeHandler()
    }

    @After
    void tearDown() {
        fileWatcher?.stop()
        watcherThread?.join(TIMEOUT_MILLIS)
        root.toFile().deleteDir()
    }

    @Test
    void "should keep watching a directory that was deleted and instantly re-created (git rebase)"() {
        def chapterDir = root.resolve("chapter")
        def pageFile = chapterDir.resolve("page.md")
        createFile(pageFile, "# original")

        startWatcher(pageFile)

        // simulate rebase: directory disappears and re-appears with content right away,
        // before the watcher had a chance to notice its watch key became invalid
        chapterDir.toFile().deleteDir()
        createFile(pageFile, "# rebased")

        waitForChange(pageFile)

        // watching must still work for subsequent modifications
        changeHandler.clear()
        writeAndTouch(pageFile, "# edited after rebase")
        waitForChange(pageFile)
    }

    @Test
    void "should resume watching a directory re-created after the watcher noticed its deletion"() {
        def chapterDir = root.resolve("chapter")
        def pageFile = chapterDir.resolve("page.md")
        createFile(pageFile, "# original")

        startWatcher(pageFile)

        chapterDir.toFile().deleteDir()
        actual(liveValue { fileWatcher.isWatched(chapterDir) }, "chapter dir is watched")
                .waitTo(equal(false), TIMEOUT_MILLIS)

        createFile(pageFile, "# rebased")
        waitForChange(pageFile)

        changeHandler.clear()
        writeAndTouch(pageFile, "# edited after rebase")
        waitForChange(pageFile)
    }

    @Test
    void "should watch nested directories created together with their parent"() {
        startWatcher(root)

        // git creates whole trees at once, nested content appears before a watch is established
        def nestedFile = root.resolve("chapter/nested/page.md")
        createFile(nestedFile, "# nested")

        waitForChange(nestedFile)

        changeHandler.clear()
        writeAndTouch(nestedFile, "# nested edited")
        waitForChange(nestedFile)
    }

    @Test
    void "should not watch hidden directories like git internals"() {
        startWatcher(root)

        def hiddenFile = root.resolve(".git/objects/blob.md")
        createFile(hiddenFile, "# hidden")

        def visibleFile = root.resolve("chapter/page.md")
        createFile(visibleFile, "# visible")

        // by the time the visible file change is noticed, the hidden one (created earlier) was processed and ignored
        waitForChange(visibleFile)
        actual(changeHandler.changedPaths.collect { it.fileName.toString() }, "changed files")
                .shouldNot(contain("blob.md"))
    }

    private void startWatcher(Path pathToWatch) {
        def cfg = WebSite.withRoot(root)
        fileWatcher = new FileWatcher(cfg, [pathToWatch].stream(), changeHandler)

        watcherThread = new Thread(() -> fileWatcher.start())
        watcherThread.daemon = true
        watcherThread.start()
    }

    private void waitForChange(Path path) {
        actual(liveValue { changeHandler.changedPaths.collect { it.fileName.toString() } }, "changed files")
                .waitTo(contain(path.fileName.toString()), TIMEOUT_MILLIS)
    }

    private static void createFile(Path path, String content) {
        Files.createDirectories(path.parent)
        Files.write(path, content.bytes)
    }

    // watch service implementations may rely on modification time with a coarse resolution,
    // bump it explicitly so an edit right after a create is guaranteed to be visible
    private static void writeAndTouch(Path path, String content) {
        Files.write(path, content.bytes)
        Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis() + 5000))
    }

    private static class RecordingFileChangeHandler implements FileChangeHandler {
        final Queue<Path> changedPaths = new ConcurrentLinkedQueue<>()

        @Override
        void onTocChange(Path path) {
            changedPaths.add(path)
        }

        @Override
        void onFooterChange(Path path) {
            changedPaths.add(path)
        }

        @Override
        void onDocMetaChange(Path path) {
            changedPaths.add(path)
        }

        @Override
        void onGlobalDocReferencesChange(Path path) {
            changedPaths.add(path)
        }

        @Override
        void onChange(Path path) {
            changedPaths.add(path)
        }

        void clear() {
            changedPaths.clear()
        }
    }
}
