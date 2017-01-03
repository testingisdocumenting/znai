package com.twosigma.testing.expectation.equality.handlers

import com.twosigma.testing.expectation.ActualPath
import com.twosigma.testing.expectation.equality.EqualComparator
import com.twosigma.testing.expectation.equality.EqualComparatorHandler
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * @author mykola
 */
class MapEqualHandlerTest {
    private EqualComparator equalComparator

    @Before
    void init() {
        equalComparator = EqualComparator.comparator()
    }

    @Test
    void "should only handle maps on both sides"() {
        EqualComparatorHandler handler = new MapEqualHandler()
        assert ! handler.handle(10, "test")
        assert ! handler.handle([k: 1], [])
        assert ! handler.handle([], [k: 1])

        assert handler.handle([k1: 1], [k2: 2])
    }

    @Test
    void "should report missing keys on both sides"() {
        equalComparator.compare(ActualPath.createActualPath("map"),
                [k6: 'v1', k2: [k21: 'v21'], k3: 'v3'],
                [k1: 'v1', k2: [k22: 'v21'], k4: 'v3'])

        println equalComparator.generateMismatchReport()
        assertEquals("", equalComparator.generateMismatchReport())
    }
}
