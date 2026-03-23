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

package scenarios

import static clicommands.CliCommands.getZnai
import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

def port = 3462

def znaiPreview

scenario("run znai preview for mermaid links") {
    znaiPreview = znai.runInBackground(
            "--source=${cfg.fullPath("sampledoc")} --port=$port --preview")
    znaiPreview.output.waitTo(contain("server started"), 20_000)
}

scenario("open mermaid diagrams page") {
    previewServer.openPreviewWithUrl(port, "chapter-three/mermaid-diagrams")
    docContent.title.waitTo == "Mermaid Diagrams"
}

scenario("click mermaid link and browser back restores scroll position") {
    docContent.sectionHeaders.get("Diagram With Links").scrollIntoView()
    docContent.mermaidClickableNodes.waitTo visible

    def scrollTopBeforeClick = docContent.mainPanelScrollTop.get()

    docContent.clickMermaidNode()

    docContent.title.waitTo == "Target"
    browser.url.path.should contain("chapter-one/target")

    browser.back()

    browser.url.path.waitTo contain("chapter-three/mermaid-diagrams")
    docContent.title.waitTo == "Mermaid Diagrams"
    docContent.mainPanelScrollTop.waitTo == scrollTopBeforeClick
}

scenario("stop mermaid preview server") {
    if (znaiPreview) {
        znaiPreview.stop()
    }
}
