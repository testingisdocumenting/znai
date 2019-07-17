package com.twosigma.znai.maven

import org.junit.Test

import static org.junit.Assert.assertEquals

class MDocCliRunnerTest {
    @Test
    void "should handle params with no values"() {
        def args = MDocCliRunner.constructArgs([
                noValue: null,
                foo: 'bar',
        ])

        String[] expectedArgs = ['--noValue', '--foo=bar']
        assertEquals(expectedArgs, args)
    }
}
