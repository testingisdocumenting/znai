/*
 * Copyright 2024 znai maintainers
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

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

def contentPort = 4901

scenario("host znai docs as static content") {
    staticServer = server.serve("main-znai-docs", "../../../../znai-docs/target", contentPort)
}

scenario("open browser with docs") {
   browser.open("http://localhost:$contentPort/znai")
}

scenario("switch to presentation mode and validate title") {
    standardView.presentationButton.click()
    presentationContent.title.waitTo == "What Is This"
}

scenario("close presentation") {
    presentationView.closeButton.click()
    docContent.title.waitTo == "What Is This"
}
