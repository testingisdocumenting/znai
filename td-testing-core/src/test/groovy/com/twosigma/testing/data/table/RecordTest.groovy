package com.twosigma.testing.data.table

import org.junit.Test

import java.util.stream.Stream

import static com.twosigma.testing.expectation.ActualValue.actual
import static com.twosigma.testing.expectation.Matchers.equal

/**
 * @author mykola
 */
class RecordTest {
    @Test
    void "should be convertible to map"() {
        def record = new Record(new Header(Stream.of("n1", "n2")), Stream.of("v1", null))
        actual(record.toMap()).should(equal([n1: 'v1', n2: null]))
    }
}
