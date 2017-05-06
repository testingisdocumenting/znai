package com.twosigma.documentation.extensions.templates

import com.twosigma.documentation.extensions.ColonDelimitedKeyValues
import org.junit.Test

/**
 * @author mykola
 */
class ColonDelimitedKeyValuesTest {
    @Test
    void "extracts key values from block of text"() {
        def values = new ColonDelimitedKeyValues("""key1:value1
key2:value21
value22
key3: value3
key4:
value41
value42
""")
        assert values.toMap() == [key1: "value1", key2: "value21\nvalue22", key3: " value3", key4: "value41\nvalue42"]

    }
}
