package com.twosigma.testing.expectation.equality.handlers

import com.twosigma.testing.expectation.equality.EqualComparator
import com.twosigma.testing.expectation.equality.EqualComparatorHandler
import org.junit.Before
import org.junit.Test

import static com.twosigma.testing.Ddjt.createActualPath
import static org.junit.Assert.assertEquals

/**
 * @author mykola
 */
class MapsEqualHandlerTest {
    private EqualComparator equalComparator

    @Before
    void init() {
        equalComparator = EqualComparator.comparator()
    }

    @Test
    void "should only handle maps on both sides"() {
        EqualComparatorHandler handler = new MapsEqualHandler()
        assert ! handler.handle(10, "test")
        assert ! handler.handle([k: 1], [])
        assert ! handler.handle([], [k: 1])

        assert handler.handle([k1: 1], [k2: 2])
    }

    @Test
    void "should report missing keys on both sides"() {
        equalComparator.compare(createActualPath("map"),
                [k6: 'v1', k2: [k21: 'v21'], k3: 'v3'],
                [k1: 'v1', k2: [k22: 'v21'], k3: 'v3-'])

        def report = equalComparator.generateMismatchReport()
        assertEquals("mismatches:\n" +
                "\n" +
                "map.k3:   actual: v3 <java.lang.String>\n" +
                "        expected: v3- <java.lang.String>\n" +
                "\n" +
                "unexpected values:\n" +
                "\n" +
                "map.k2.k21: v21\n" +
                "map.k6: v1", report)
    }
}
