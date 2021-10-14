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

package handlers

import clicommands.CliCommands
import data.FsLocations
import org.testingisdocumenting.webtau.TestListener
import pages.PreviewServer

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

class ServersStartTestListener implements TestListener {
    @Override
    void beforeFirstTest() {
        step("start static server") {
            startStaticServer()
        }

        step("start preview server") {
            startPreviewServer()
        }
    }

    private static void startStaticServer() {
        // during development we connect to Create React App hosted site
        def craBaseUrl = "http://localhost:3000/preview"
        if (http.ping("${craBaseUrl}/index.html")) {
            cfg.setBaseUrl("cra", craBaseUrl)
            return
        }

        // if no CRA around, we start static server
        def server = server.serve("znai-docs", "../../../../znai-docs/target")
        cfg.setBaseUrl("static-server", server.baseUrl + "/znai")
    }

    private static void startPreviewServer() {
        def previewServerUrl = "http://localhost:3334/preview"
        if (http.ping("${previewServerUrl}/index.html")) {
            PreviewServer.port = 3334
            return
        }

        def cliPreviewPort = 3458
        def znaiPreview = CliCommands.znai.runInBackground("--preview --port ${cliPreviewPort}" +
                " --lookup-paths znai-overrides",
                cli.workingDir(FsLocations.znaiDocsRoot()))

        znaiPreview.output.waitTo(contain("server started"), 30_000)
        PreviewServer.port = cliPreviewPort
    }
}
