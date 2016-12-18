package com.twosigma.testing.data.table

import org.junit.Test

/**
 * @author mykola
 */
class CompositeKeyTest {
    @Test
    void "should equal when all the parts are equal"() {
        def key1 = new CompositeKey(['a', 'b'])
        def key2 = new CompositeKey(['a', 'b'])

        assert key1 == key2
        assert key1.hashCode() == key2.hashCode()
    }

    @Test
    void "hash code should be calculated for all parts of the key"() {
        assert new CompositeKey([10]).hashCode() != new CompositeKey([10, 20]).hashCode()
    }

    @Test
    void "order of parts should matter"() {
        def key1 = new CompositeKey([10, 20])
        def key2 = new CompositeKey([20, 10])

        assert key1 != key2
        assert key1.hashCode() != key2.hashCode()
    }
}
