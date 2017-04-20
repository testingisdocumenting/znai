package com.twosigma.testing.standalone

import com.twosigma.utils.ResourceUtils
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class StandaloneTestRunnerTest {
    private static StandaloneTestRunner runner = createRunner()

    @Test
    void "should register tests with scenario keyword"() {
        runner.tests.description.should == ['# first header\noptionally split into multiple lines and has a header\n',
                                            '# second header\noptionally split into multiple lines and has a header\n',
                                            '# third header\noptionally split into multiple lines and has a header\n']
    }

    @Test
    void "should mark test as failed, passed or errored"() {
        runner.runTests()

        runner.tests[0].hasError().should == true
        runner.tests[0].isFailed().should == false
        runner.tests[0].exception.message.should == "error on purpose"

        runner.tests[1].hasError().should == false
        runner.tests[1].isFailed().should == true
        runner.tests[1].assertionMessage.should == "\n[value]   actual: 10 <java.lang.Integer>\n" +
                "[value] expected: 11 <java.lang.Integer> [reported by AnyEqualHandler]"

        runner.tests[2].hasError().should == false
        runner.tests[2].isFailed().should == false
    }

    private static StandaloneTestRunner createRunner() {
        def runner = new StandaloneTestRunner()
        runner.process(Paths.get("/test/path.groovy"),
                ResourceUtils.textContent("StandaloneTest.groovy"))

        return runner
    }
}
