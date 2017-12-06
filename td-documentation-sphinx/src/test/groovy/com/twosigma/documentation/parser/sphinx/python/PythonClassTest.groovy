package com.twosigma.documentation.parser.sphinx.python

import org.junit.Test

import static com.twosigma.testing.Ddjt.actual
import static com.twosigma.testing.Ddjt.equal

/**
 * @author mykola
 */
class PythonClassTest {
    @Test
    void "should convert to map"() {
        def pythonClass = new PythonClass('ClassName', 'class description')
        def method1 = new PythonMethod('methodOne', 'method one description')
        method1.addParam(new PythonMethodParam('param1', 'param1 description'))
        method1.addParam(new PythonMethodParam('param2', 'param2 description'))

        def method2 = new PythonMethod('methodTwo', 'method two description')
        method2.addParam(new PythonMethodParam('param2', 'param2 description'))

        pythonClass.addMethod(method1)
        pythonClass.addMethod(method2)

        def map = pythonClass.toMap()
        actual(map).should(equal([name: 'ClassName', description: 'class description',
                                  methods:[[name: 'methodOne', description: 'method one description',
                                                            params:[[name: 'param1', description: 'param1 description'],
                                                                    [name: 'param2', description: 'param2 description']]],
                                                           [name: 'methodTwo', description: 'method two description',
                                                             params:[[name: 'param2', description: 'param2 description']]]]]))
    }
}
