package com.twosigma.documentation.extensions.cli

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.testing.Ddjt.equal

/**
 * @author mykola
 */
class CliOutputIncludePluginTest {
    @Test
    void "should split file content into lines"() {
        def elements = process("captured-output.out")
        elements.should equal(['lines': ['line one', 'line two', 'line three'], highlight:[], type: 'CliOutput'])
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-cli-output: $params")
        return result[0].toMap()
    }
}
