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

package scenarios

import org.testingisdocumenting.webtau.cli.CliBackgroundCommand
import org.testingisdocumenting.webtau.utils.JsonUtils

import static clicommands.CliCommands.znai
import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.hubPortal

CliBackgroundCommand znaiBackground

def zippedDocsCache = cache.value('hub-zipped-doc-path')
def storedDocsDirCache = cache.value('hub-stored-docs-dir')

def port = 3456

scenario("start hub") {
    def enterpriseDir = fs.tempDir("znai-enterprise")

    def storedDocsDir = enterpriseDir.resolve("stored-docs")
    fs.createDir(storedDocsDir)

    storedDocsDirCache.set(storedDocsDir.toString())

    fs.writeText(enterpriseDir.resolve("znai-enterprise.cfg"), JsonUtils.serialize([
            znaiDocStoragePath: storedDocsDir.toString()
    ]))

    znaiBackground = znai.runInBackground("--serve --port=$port", cli.workingDir(enterpriseDir))
    znaiBackground.output.waitTo contain("server started")
}

scenario("generate static doc") {
    def docsDir = fs.tempDir("generated-doc")
    def zipParentDir = fs.tempDir("zip-dir")
    fs.writeText(docsDir.resolve("index.html"), "<body>test docs</body>")
    fs.writeText(docsDir.resolve("meta.json"), JsonUtils.serialize([
            id: "my-doc-id",
            title: "My Docs",
            description: "My Description",
            category: "Research"
    ]))

    step("zipping docs") {
        def zipPath = zipParentDir.resolve("my-doc-id.zip")
        zippedDocsCache.set(zipPath.toString())
        fs.zip(docsDir, zipPath)
    }
}

scenario("upload docs via cli") {
    println zippedDocsCache.get()
    znai.run("--deploy=${zippedDocsCache.get()} --doc-id=my-doc-id --uploadzip",
            cli.env(ZNAI_SERVER_URL: "http://localhost:$port"))
}

scenario("check docs on filesystem") {
    // TODO use waitTo when fixed in new version of webtau
    sleep 3000
    fs.textContent("${storedDocsDirCache.get()}/my-doc-id/index.html").should == "<body>test docs</body>"
}

scenario("check docs on hub portal") {
    browser.open("http://localhost:$port")
    def myDocs = hubPortal.names.get("My Docs")
    myDocs.waitTo beVisible()
    myDocs.click()

    $("body").waitTo == "test docs"
}

scenario("stop hub") {
    if (znaiBackground) {
        znaiBackground.stop()
    }
}
