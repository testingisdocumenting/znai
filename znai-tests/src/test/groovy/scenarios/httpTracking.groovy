/*
 * Copyright 2025 znai maintainers
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
import org.testingisdocumenting.webtau.server.WebTauServer

import static clicommands.CliCommands.znai
import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

def port = 3460
def trackingServerPort = 3461
def capturedEvents = Collections.synchronizedList([])

WebTauServer trackingServer
CliBackgroundCommand znaiPreview

scenario("start tracking fake server") {
    def router = server.router()
            .post("/track") { request ->
                def content = request.getContentAsMap()
                capturedEvents.add(content)
                return server.response([status: "ok"])
            }

    trackingServer = server.fake("tracking-server", trackingServerPort, router)
}

scenario("run znai preview with tracking enabled") {
    znaiPreview = znai.runInBackground(
            "--source=${cfg.fullPath("sampledoc")} --port=$port --preview",
            cli.env(ZNAI_TRACK_ACTIVITY_URL: "http://localhost:$trackingServerPort/track"))
    znaiPreview.output.waitTo(contain("server started"), 20_000)
}

scenario("validate initial page load sends tracking event") {
    previewServer.openPreviewWithUrl(port, "chapter-one/target")
    docContent.title.waitToBe visible

    actual(liveValue{ -> capturedEvents.size() }).waitToBe(greaterThanOrEqual(1))

    def pageOpenEvent = capturedEvents.find { it.eventType == "pageOpen" }
    pageOpenEvent.pageId.should == "chapter-one/target"
}

scenario("validate TOC navigation sends tracking events") {
    standardView.tocItems.get("Links").click()
    docContent.title.waitTo == "Links"

    def tocSelectEvents = capturedEvents.findAll { it.eventType == "tocItemSelect" }
    tocSelectEvents.size().shouldBe >= 1
}

scenario("validate another TOC navigation") {
    standardView.tocItems.get("Target").click()
    docContent.title.waitTo == "Target"

    def tocSelectEvents = capturedEvents.findAll { it.eventType == "tocItemSelect" }
    tocSelectEvents.size().waitTo >= 2
}

scenario("validate all captured events") {
    def pageOpenEvents = capturedEvents.findAll { it.eventType == "pageOpen" }
    pageOpenEvents.size().shouldBe >= 2

    def pageIds = pageOpenEvents.collect { it.pageId }
    pageIds.should contain("chapter-one/links")
    pageIds.should contain("chapter-one/target")
}

scenario("stop servers") {
    if (znaiPreview) {
        znaiPreview.stop()
    }
    if (trackingServer) {
        trackingServer.stop()
    }
}
