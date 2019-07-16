package com.twosigma.documentation.extensions.templates

import com.twosigma.documentation.extensions.ColonDelimitedKeyValues
import org.junit.Test

import static com.twosigma.webtau.Ddjt.equal

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
        values.toMap().should equal([key1: "value1", key2: "value21\nvalue22", key3: " value3", key4: "value41\nvalue42"])
    }

    @Test
    void "supports keys that contain non alphabetic symbols"() {
        def values = new ColonDelimitedKeyValues("key@1:value1\nkey@2:value2")
        values.toMap().should equal(["key@1": "value1", "key@2": "value2"])
    }

    @Test
    void "supports keys that contain spaces but only when in quotes"() {
        def values = new ColonDelimitedKeyValues("\"key with space\":value1\n" +
                "key 2:value2")
        values.toMap().should equal(["key with space": "value1\nkey 2:value2"])
    }

    @Test
    void "key should not start with a space"() {
        def values = new ColonDelimitedKeyValues(" key:value1")
        values.toMap().should equal([:])
    }
}
