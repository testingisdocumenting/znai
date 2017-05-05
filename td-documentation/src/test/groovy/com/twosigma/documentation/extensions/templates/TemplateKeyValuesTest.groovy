package com.twosigma.documentation.extensions.templates

import org.junit.Test

/**
 * @author mykola
 */
class TemplateKeyValuesTest {
    @Test
    void "extracts key values from block of text"() {
        def values = new TemplateKeyValues("""key1:value1
key2:value21
value22
key3: value3
""")
        assert values.toMap() == [key1: "value1", key2: "value21\nvalue22", key3: " value3"]

    }
}
