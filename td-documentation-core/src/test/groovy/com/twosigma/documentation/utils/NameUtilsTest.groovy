package com.twosigma.documentation.utils

import org.junit.Test

import static com.twosigma.documentation.utils.NameUtils.idFromTitle
import static com.twosigma.documentation.utils.NameUtils.dashToCamelCaseWithSpaces

class NameUtilsTest {
    @Test
    void "converts title to id stripping spaces and punctuation"() {
        assert idFromTitle(null) == null
        assert idFromTitle('') == ''
        assert idFromTitle('word') == 'word'
        assert idFromTitle('Hello DearWorld') == 'hello-dearworld'
        assert idFromTitle('Hello!. %#$ DearWorld?') == 'hello-dearworld'
    }

    @Test
    void "converts dashes to camel case with spaces"() {
        assert dashToCamelCaseWithSpaces(null) == null
        assert dashToCamelCaseWithSpaces('') == ''
        assert dashToCamelCaseWithSpaces('word') == 'Word'
        assert dashToCamelCaseWithSpaces('hello-dear-world') == 'Hello Dear World'
    }
}
