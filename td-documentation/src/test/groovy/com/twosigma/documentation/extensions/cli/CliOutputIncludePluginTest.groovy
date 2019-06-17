package com.twosigma.documentation.extensions.cli

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.equal
import static com.twosigma.webtau.Ddjt.throwException

/**
 * @author mykola
 */
class CliOutputIncludePluginTest {
    @Test
    void "should split file content into lines"() {
        def elements = process('captured-output.out')
        elements.should equal(['lines': ['line one', 'line two', 'line three'], highlight: [], type: 'CliOutput'])
    }

    @Test
    void "should convert text to indexes with full match"() {
        def elements = process('captured-output.out {highlight: "line two"}')
        elements.highlight.should equal([1])
    }

    @Test
    void "should convert text to indexes with partial match"() {
        def elements = process('captured-output.out {highlight: "two"}')
        elements.highlight.should equal([1])
    }

    @Test
    void "should error when no text to highlight found"() {
        code {
            process('captured-output.out {highlight: "line x"}')
        } should throwException("can't find line: line x")
    }

    @Test
    void "should read lines to highlight from file"() {
        def elements = process('captured-output.out {highlightFile: "captured-matched-lines.txt"}')
        elements.highlight.should equal([1, 2])
    }

    @Test
    void "should error when lines to highlight from file are not present"() {
        code {
            process('captured-output.out {highlightFile: "captured-matched-lines-error.txt"}')
        } should throwException("can't find line: wrong line two")
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-cli-output: $params")
        return result[0].toMap()
    }
}
