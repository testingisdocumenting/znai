package com.twosigma.testing.webtau.reporter

import org.junit.Test

/**
 * @author mykola
 */
class HtmlReportGeneratorTest {
    @Test
    void "generates html using prebuilt javascript libs"() {
        def generator = new HtmlReportGenerator()
        def html = generator.generate("{summary: 'summary'}")

        assert html.contains(".list-of-tests")
        assert html.contains("testReport = {summary: 'summary'}")
        assert html.contains("Minified React error")
    }
}
