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

import com.twosigma.znai.utils.ResourceUtils
import org.junit.BeforeClass
import org.junit.Test

class PythonClassXmlParserTest {
    static def xml = ResourceUtils.textContent('auto-class.xml')
    static PythonClass pythonClass

    @BeforeClass
    static void init() {
        def parser = new PythonClassXmlParser()
        pythonClass = parser.parse(xml)
    }

    @Test
    void "parse class name and description"() {
        pythonClass.should == [refId: 'world.HelloWorld', name: 'HelloWorld', description: 'Simple hello world class']
    }

    @Test
    void "parse method name, description and params"() {
        def params = [ 'name' | 'description'] {
                      _______________________________________
                       'name' | 'name of a person to greet'
                      'title' | 'title of a person to greet' }

        pythonClass.methods.should == [                 'refId'   |   'name' | 'description' | 'params'] {
                                      ________________________________________________________________
                                        'world.HelloWorld.sayBye' | 'sayBye' |    'says bye' | params
                                         'world.HelloWorld.sayHi' | 'sayHi'  |    'says hi'  | params }
    }
}
