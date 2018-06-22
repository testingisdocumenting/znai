package com.twosigma.utils

import org.junit.Test

class YamlUtilsTest {
    @Test
    void "deserializes yaml document as map"() {
        def yaml = """
hello: world
items:
    - one
    - two
    - three
number: 10    
"""
        assert YamlUtils.deserializeAsMap(yaml) == [hello: 'world', items: ['one', 'two', 'three'], number: 10]
    }
}
