package com.twosigma.utils

import org.junit.Test

/**
 * @author mykola
 */
class JsonUtilsTest {
    @Test
    void "should serialize json using compact print"() {
        def asText = JsonUtils.serialize([k1: "v1", k2: ["v2", "v3"]])
        assert asText == '{"k1":"v1","k2":["v2","v3"]}'
    }

    @Test
    void "should serialize json using pretty print"() {
        def asText = JsonUtils.serializePrettyPrint([k1: "v1", k2: ["v2", "v3"]])
        assert asText == '{\n' +
                '  "k1": "v1",\n' +
                '  "k2": [\n' +
                '    "v2",\n' +
                '    "v3"\n' +
                '  ]\n' +
                '}'
    }
}
