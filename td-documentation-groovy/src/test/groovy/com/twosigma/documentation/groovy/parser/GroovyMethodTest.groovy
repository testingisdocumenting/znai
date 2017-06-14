package com.twosigma.documentation.groovy.parser

import org.junit.Test

/**
 * @author mykola
 */
class GroovyMethodTest {
    @Test
    void "ignores spaces in types definition when compares name and types"() {
        def method = new GroovyMethod(name: "name", nameWithTypes: "name(int,boolean)")
        method.matchesNameAndType("name").should == true
        method.matchesNameAndType("name(int,boolean)").should == true
        method.matchesNameAndType("name(int, boolean)").should == true

        method.matchesNameAndType("name()").should == false
    }
}
