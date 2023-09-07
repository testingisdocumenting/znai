/*
 * Copyright 2022 znai maintainers
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

def port = 3459
scenario("run znai preview") {
    def znaiPreview = znai.runInBackground(
            "--source=${cfg.fullPath("sampledoc")} --port=$port --preview")
    znaiPreview.output.waitTo(contain("server started"), 20_000)
}

scenario("validate doxygen page") {
    previewServer.openPreview(port)
    standardView.tocItems.get("Doxygen From Zip").click()
    docContent.paragraphs.get("list three").waitTo visible
}

sscenario("validate uploads files") {
    def baseUrl = "http://localhost:$port/preview"

    http.get("$baseUrl/extra-dir/file-three.txt") {
        body.should == "file-three"
    }

    http.get("$baseUrl/extra-dir/file-four.json") {
        message.should == "file-four"
    }

    http.get("$baseUrl/extra-files/file-one.txt") {
        body.should == "file-one"
    }
}
