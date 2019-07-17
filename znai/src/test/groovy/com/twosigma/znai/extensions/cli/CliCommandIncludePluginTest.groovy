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
