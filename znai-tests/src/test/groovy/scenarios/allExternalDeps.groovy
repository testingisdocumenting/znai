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

scenario('build sampledoc with all external dependencies file') {
    def depsFilePath = fs.tempDir('znai-deps').resolve('all-external-deps.txt')
    def deployRoot = fs.tempDir('znai-deploy')

    znai.run("build --doc-id test-doc --deploy $deployRoot " +
            "--source=${cfg.fullPath('sampledoc')} " +
            "--all-external-dependencies-file-path $depsFilePath")

    fs.textContent(depsFilePath).should == '../test-doc-artifacts/app.config\n' +
            '../test-doc-artifacts/schema.graphql\n' +
            'chapter-one/config.json\n' +
            'chapter-two/sample.py\n' +
            'utils.js'
}
