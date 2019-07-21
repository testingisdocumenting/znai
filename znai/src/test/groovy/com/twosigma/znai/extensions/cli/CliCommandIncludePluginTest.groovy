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

package com.twosigma.znai.extensions.cli

import com.twosigma.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.webtau.Ddjt.equal

class CliCommandIncludePluginTest {
    @Test
    void "passes through params to highlight to the doc element"() {
        def elements = process("git meta push origin HEAD:myfeature/pushrequest {paramToHighlight: \"push\"}")
        elements.should equal([command: 'git meta push origin HEAD:myfeature/pushrequest', paramsToHighlight: ['push'], type: 'CliCommand'])
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-cli-command: $params")
        return result[0].toMap()
    }
}
