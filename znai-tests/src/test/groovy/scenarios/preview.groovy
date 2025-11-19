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

import clicommands.CliCommands

import java.nio.file.Path

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

def scaffoldedPathCache = cache.value('scaffolded-docs-for-preview')

scenario('scaffold docs and run preview') {
    def scaffoldPath = fs.tempDir('znai-scaffold-for-preview')
    CliCommands.znai.run('new', cli.workingDir(scaffoldPath))

    def docsPath = scaffoldPath.resolve("guide")
    scaffoldedPathCache.set(docsPath.toString())

    def port = 3457
    def preview = CliCommands.znai.runInBackground("preview --port=${port}", cli.workingDir(docsPath))

    preview.output.waitTo(contain("server started"), 30_000)

    previewServer.openPreview(port)
}

scenario('footer should be updated on footer file change') {
    standardView.footer.should == ~/Contributions are welcome/

    def docsPath = scaffoldedPathCache.getAsPath()
    fs.writeText(docsPath.resolve("footer.md"), "new footer")
    standardView.footer.waitTo == "new footer"
}

scenario('toc update should redeploy all pages json and new page link should be active on page reload') {
    def docsPath = scaffoldedPathCache.getAsPath()
    fs.createDir(docsPath.resolve("new-chapter"))
    fs.writeText(docsPath.resolve("new-chapter/new-page.md"), "new page content")

    def tocPath = docsPath.resolve("toc")
    def tocContent = fs.textContent(tocPath).data
    fs.writeText(tocPath, tocContent + "\nnew-chapter\n    new-page.md")

    def newPageTocItem = standardView.tocItems.get("New Page")
    newPageTocItem.waitToBe visible
    standardView.pageTitle.should == "New Page"

    standardView.pageTwoTocItem.click()
    browser.refresh()

    newPageTocItem.click()
    standardView.pageTitle.waitTo == "New Page"
}

scenario('preview jumps to a page associated with a change') {
    def docsPath = scaffoldedPathCache.getAsPath()

    standardView.pageThreeTocItem.click()

    def gettingStartedTitle = 'Getting Started'
    standardView.pageTitle.shouldNot == gettingStartedTitle

    def gettingStartedPath = docsPath.resolve("chapter-one/getting-started.md")
    replaceText(gettingStartedPath, "Each documentation must have ", "Each documentation must have!")

    standardView.pageTitle.waitTo == gettingStartedTitle
    browser.url.path.should == "/preview/chapter-one/getting-started"

    // TODO replace with `inViewport` matcher when webtau releases it
    // TODO uncomment after fix of race
    // standardView.mainPanelScrollTop.shouldBe > 400
}

static def replaceText(Path path, String regexp, String replacement) {
    step("replace text in $path") {
        def fullPath = cfg.fullPath(path)
        def text = fs.textContent(fullPath).data
        def replaced = text.replaceAll(regexp, replacement)

        fs.writeText(fullPath, replaced)
    }
}