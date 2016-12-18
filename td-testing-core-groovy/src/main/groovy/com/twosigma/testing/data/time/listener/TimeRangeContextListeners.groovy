package com.twosigma.testing.data.time.listener

import com.twosigma.testing.data.time.TestTime
import com.twosigma.utils.ServiceUtils
/**
 * @author mykola
 */
class TimeRangeContextListeners {
    // TODO multithread support
    private static List<TimeRangeContextListener> listeners = ServiceUtils.discover(TimeRangeContextListener)

    static void clear() {
        listeners.clear()
    }

    static void add(TimeRangeContextListener listener) {
        listeners.add(listener)
    }

    static void beforeCall(TestTime begin, TestTime end) {
        listeners.each { it.beforeCall(begin, end) }
    }

    static void afterCall(TestTime begin, TestTime end) {
        listeners.each { it.afterCall(begin, end) }
    }
}
