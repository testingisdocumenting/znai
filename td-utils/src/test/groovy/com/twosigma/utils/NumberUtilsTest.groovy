package com.twosigma.utils

import org.junit.Test

/**
 * @author mykola
 */
class NumberUtilsTest {
    @Test
    void "should convert double as string to double"() {
        def number = NumberUtils.convertStringToNumber("12.45")
        assert number.class == Double
        assert number == 12.45
    }

    @Test
    void "should convert number without decimals as string to long"() {
        def number = NumberUtils.convertStringToNumber("12")
        assert number.class == Long
        assert number == 12
    }

    @Test
    void "should detect integers"() {
        assert NumberUtils.isInteger("100")
        assert NumberUtils.isInteger("0")

        assert !NumberUtils.isInteger("")
        assert !NumberUtils.isInteger("10.0")
        assert !NumberUtils.isInteger("ab")
    }
}
