package com.twosigma.testing.data.time.listener

import com.twosigma.testing.data.time.TestTime

/**
 * @author mykola
 */
interface TimeRangeContextListener {
    void beforeCall(TestTime begin, TestTime end)
    void afterCall(TestTime begin, TestTime end)
}
