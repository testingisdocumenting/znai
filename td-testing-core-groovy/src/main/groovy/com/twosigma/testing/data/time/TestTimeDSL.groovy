package com.twosigma.testing.data.time

/**
 * @author mykola
 */
class TestTimeDSL {
    static TestTime today(Map hoursMinutesMap) {
        def hm = TestTimeDSLUtils.hoursMinutesFromMap(hoursMinutesMap)
        return TestTime.today(hm.hours, hm.minutes)
    }
}
