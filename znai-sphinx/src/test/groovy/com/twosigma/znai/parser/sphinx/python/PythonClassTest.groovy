package com.twosigma.znai.parser.sphinx.python

import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.equal

class PythonClassTest {
    @Test
    void "should convert to map"() {
        def pythonClass = new PythonClass('classname', 'ClassName', 'class description')
        def method1 = new PythonFunction('classname.methodOne', 'methodOne', 'method one description')
        method1.addParam(new PythonFunctionParam('param1', 'str', 'param1 description'))
        method1.addParam(new PythonFunctionParam('param2', 'str', 'param2 description'))

        def method2 = new PythonFunction('classname.methodTwo', 'methodTwo', 'method two description')
        method2.addParam(new PythonFunctionParam('param2',  'str', 'param2 description'))

        pythonClass.addMethod(method1)
        pythonClass.addMethod(method2)

        def map = pythonClass.toMap()
        actual(map).should(equal([refId: 'classname', name: 'ClassName', description: 'class description',
                                  methods:[[refId: 'classname.methodOne', name: 'methodOne', description: 'method one description',
                                            params:[[name: 'param1', type: 'str', description: 'param1 description'],
                                                    [name: 'param2', type: 'str', description: 'param2 description']],
                                            paramSignatures: []],
                                          [refId: 'classname.methodTwo', name: 'methodTwo', description: 'method two description',
                                           params:[[name: 'param2', type: 'str', description: 'param2 description']],
                                           paramSignatures: []]]]))
    }
}
