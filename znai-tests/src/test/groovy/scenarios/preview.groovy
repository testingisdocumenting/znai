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

import data.FsLocations

import java.nio.file.Path

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario('open preview') {
    previewServer.openPreview()
}

scenario('footer should be updated on footer file change') {
    standardView.footer.should == ~/Contributions are welcome/

    fs.writeText(FsLocations.resolveFromZnaiDocs("footer.md"), "new footer")
    standardView.footer.waitTo == "new footer"
}

scenario('preview jumps to a page associated with a change') {
    def externalCodeSnippetsTitle = 'External Code Snippets'

    standardView.pageTitle.shouldNot == externalCodeSnippetsTitle

    def externalCodePath = FsLocations.resolveFromZnaiDocs("snippets/external-code-snippets.md")
    replaceText(externalCodePath, "Given file with inlined comments", "Given file with inlined comments!")

    standardView.pageTitle.waitTo == externalCodeSnippetsTitle
    browser.url.path.should == "/preview/snippets/external-code-snippets"

    // TODO replace with `inViewport` matcher when webtau releases it
    standardView.mainPanelScrollTop.shouldBe > 400
}

static def replaceText(Path path, String regexp, String replacement) {
    step("replace text in $path") {
        def fullPath = cfg.fullPath(path)
        def text = fs.textContent(fullPath).data
        def replaced = text.replaceAll(regexp, replacement)

        fs.writeText(fullPath, replaced)
    }
}