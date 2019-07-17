package com.twosigma.znai.parser.sphinx.python

import com.twosigma.utils.ResourceUtils
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
