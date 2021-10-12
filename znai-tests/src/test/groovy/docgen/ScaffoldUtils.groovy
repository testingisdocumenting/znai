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

package docgen

import clicommands.CliCommands
import org.testingisdocumenting.webtau.server.WebtauServer

import static org.testingisdocumenting.webtau.WebTauDsl.*

class ScaffoldUtils {
    static WebtauServer scaffoldAndServe(String serverId) {
        def tempPath = fs.tempDir('znai-scaffold')
        CliCommands.znai.run('--new', cli.workingDir(tempPath))

        def znaiDeployRoot = tempPath.resolve('deploy-root')
        CliCommands.znai.run("--doc-id my-product --deploy $znaiDeployRoot",
                cli.workingDir(tempPath.resolve('znai')))

        return server.serve(serverId,
                znaiDeployRoot
                        .toRealPath())  // to remove symlink, todo remove after webtau release
    }
}
