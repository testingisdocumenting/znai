package com.twosigma.testing.http.datanode

import com.twosigma.utils.ResourceUtils
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

/**
 * Testing REST testing engine itself
 * @author mykola
 */
class RestTestingTest {
    private static ScriptsTestRunner runner = new ScriptsTestRunner()

    @BeforeClass()
    static void init() {
        runner.before()
    }

    @AfterClass()
    static void cleanUp() {
        runner.after()
    }

    @Test
    void "should capture scenario description"() {
        runner.run(ResourceUtils.textContent("simple-rest-test.groovy"))
    }
}
