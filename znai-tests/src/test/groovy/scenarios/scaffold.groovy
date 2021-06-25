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

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

def scaffoldServerUrl = cache.value('scaffold-server-url')

scenario('scaffold new docs and serve') {
    def tempPath = fs.tempDir('znai-scaffold')
    CliCommands.znai.run('--new', cli.workingDir(tempPath))

    def znaiDeployRoot = tempPath.resolve('deploy-root')
    CliCommands.znai.run("--doc-id my-product --deploy $znaiDeployRoot",
            cli.workingDir(tempPath.resolve('znai')))

    def znaiServer = server.serve('scaffolded-static', znaiDeployRoot.resolve('my-product'))
    scaffoldServerUrl.set(znaiServer.baseUrl)
}

scenario('open docs') {
    browser.open(scaffoldServerUrl.get())
}

scenario('table of contents navigation') {
    standardView.tocSectionTitles.should containAll("INTRODUCTION", "SNIPPETS")
    standardView.shortcutsTocItem.scrollIntoView()
    standardView.externalCodeSnippetsTocItem.click()

    browser.url.path.should contain("/snippets/external-code-snippets")
    browser.title.should == "Znai: External Code Snippets"
}