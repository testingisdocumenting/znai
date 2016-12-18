package com.twosigma.testing.data.time

import org.junit.Test

import static java.time.Month.JANUARY

/**
 * @author mykola
 */
class TestTimeTest {
    @Test
    void "should convert date and time part to millis using UTC as a default time zone"() {
        assert new TestTime(1970, JANUARY, 1, 0, 0, 0).toMillisSinceEpoch() == 0
    }
}
