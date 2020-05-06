/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.extensions.cli

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class CliCommandIncludePluginTest {
    @Test
    void "passes through params to highlight to the doc element"() {
        def elements = process('git meta push origin HEAD:myfeature/pushrequest ' +
                '{paramToHighlight: "push", splitAfter: ["origin"], threshold: 60, presentationThreshold: 40}')
        elements.should == [
                command: 'git meta push origin HEAD:myfeature/pushrequest',
                paramsToHighlight: ['push'],
                splitAfter: ['origin'],
                threshold: 60,
                presentationThreshold: 40,
                type: 'CliCommand']
    }
    
    @Test
    void "validates split after tokens"() {
        code {
            process('git meta push origin HEAD:myfeature/pushrequest {splitAfter: ["origina"]}')
        } should throwException('split part "origina" is not present in command: git meta push origin HEAD:myfeature/pushrequest')
    }

    @Test
    void "validates params to highlight presence"() {
        code {
            process('git meta push origin HEAD:myfeature/pushrequest {paramsToHighlight: ["pusha"]}')
        } should throwException('param to highlight "pusha" is not present in command: git meta push origin HEAD:myfeature/pushrequest')
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-cli-command: $params")
        return result[0].toMap()
    }
}
