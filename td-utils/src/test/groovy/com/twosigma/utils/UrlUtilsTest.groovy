package com.twosigma.utils

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class UrlUtilsTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none()

    @Test
    void "url concatenation validates passed url on the left to be non null"() {
        exception.expect(IllegalArgumentException)
        exception.expectMessage('passed url on the left is NULL')

        UrlUtils.concat(null, '/relative')
    }

    @Test
    void "url concatenation validates passed url on the right to be non null"() {
        exception.expect(IllegalArgumentException)
        exception.expectMessage('passed url on the right is NULL')

        UrlUtils.concat('/relative', null)
    }

    @Test
    void "url concatenation handles slashes to avoid double slash or slash absense"() {
        def expected = 'https://base/relative'

        assert UrlUtils.concat('https://base/', '/relative') == expected
        assert UrlUtils.concat('https://base', '/relative') == expected
        assert UrlUtils.concat('https://base/', 'relative') == expected
        assert UrlUtils.concat('https://base', 'relative') == expected
    }
}
