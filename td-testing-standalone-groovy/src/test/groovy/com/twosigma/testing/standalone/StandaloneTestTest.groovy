package com.twosigma.testing.standalone

import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.testing.Ddjt.equal

/**
 * @author mykola
 */
class StandaloneTestTest {
    @Test
    void "registered payloads should be merged into toMap test representation"() {
        def test = new StandaloneTest(Paths.get("test.groovy"), "my test description", {})
        test.addResultPayload({ return [screenshot: "base64" ]})
        test.addResultPayload({ return [steps: ["step1", "step2"] ]})

        test.toMap().should equal([id: 'test.groovy-1',
                                   scenario: 'my test description',
                                   fileName: 'test.groovy',
                                   assertion: null,
                                   contextDescription: null,
                                   shortStackTrace: null,
                                   fullStackTrace: null,
                                   exceptionMessage: null,
                                   status: 'Passed',
                                   screenshot: 'base64', steps: ['step1', 'step2']])
    }
}
