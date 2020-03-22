/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.znai.parser.sphinx.python

import org.junit.Test

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
        map.should == [refId: 'classname', name: 'ClassName', description: 'class description',
                                  methods:[[refId: 'classname.methodOne', name: 'methodOne', description: 'method one description',
                                            params:[[name: 'param1', type: 'str', description: 'param1 description'],
                                                    [name: 'param2', type: 'str', description: 'param2 description']],
                                            paramSignatures: []],
                                          [refId: 'classname.methodTwo', name: 'methodTwo', description: 'method two description',
                                           params:[[name: 'param2', type: 'str', description: 'param2 description']],
                                           paramSignatures: []]]]
    }
}
